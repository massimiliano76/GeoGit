package org.geogit.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geogit.api.ObjectId;
import org.geogit.api.RevCommit;
import org.geogit.api.RevObject;
import org.geogit.api.plumbing.RevObjectParse;
import org.geogit.api.porcelain.BranchCreateOp;
import org.geogit.api.porcelain.CheckoutOp;
import org.geogit.api.porcelain.CloneOp;
import org.geogit.api.porcelain.CommitOp;
import org.geogit.api.porcelain.LogOp;
import org.geogit.remote.RemoteRepositoryTestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opengis.feature.Feature;

import com.google.common.base.Optional;

public class CloneOpTest extends RemoteRepositoryTestCase {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    protected void setUpInternal() throws Exception {
    }

    @Test
    public void testClone() throws Exception {
        // Commit several features to the remote
        List<Feature> features = Arrays.asList(points1, lines1, points2, lines2, points3, lines3);
        LinkedList<RevCommit> expected = new LinkedList<RevCommit>();

        for (Feature f : features) {
            ObjectId oId = insertAndAdd(remoteGeogit.geogit, f);
            final RevCommit commit = remoteGeogit.geogit.command(CommitOp.class).call();
            expected.addFirst(commit);
            Optional<RevObject> childObject = remoteGeogit.geogit.command(RevObjectParse.class)
                    .setObjectId(oId).call();
            assertTrue(childObject.isPresent());
        }

        // Make sure the remote has all of the commits
        Iterator<RevCommit> logs = remoteGeogit.geogit.command(LogOp.class).call();
        List<RevCommit> logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expected, logged);

        // Make sure the local repository has no commits prior to clone
        logs = localGeogit.geogit.command(LogOp.class).call();
        assertNotNull(logs);
        assertFalse(logs.hasNext());

        // clone from the remote
        CloneOp clone = clone();
        clone.setDepth(0);
        clone.setRepositoryURL(remoteGeogit.envHome.getCanonicalPath()).call();

        // Make sure the local repository got all of the commits
        logs = localGeogit.geogit.command(LogOp.class).call();
        logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expected, logged);
    }

    @Test
    public void testShallowClone() throws Exception {
        // Commit several features to the remote
        List<Feature> features = Arrays.asList(points1, lines1, points2, lines2, points3, lines3);
        LinkedList<RevCommit> expected = new LinkedList<RevCommit>();

        for (Feature f : features) {
            ObjectId oId = insertAndAdd(remoteGeogit.geogit, f);
            final RevCommit commit = remoteGeogit.geogit.command(CommitOp.class).call();
            expected.addFirst(commit);
            Optional<RevObject> childObject = remoteGeogit.geogit.command(RevObjectParse.class)
                    .setObjectId(oId).call();
            assertTrue(childObject.isPresent());
        }

        // Make sure the remote has all of the commits
        Iterator<RevCommit> logs = remoteGeogit.geogit.command(LogOp.class).call();
        List<RevCommit> logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expected, logged);

        // Make sure the local repository has no commits prior to clone
        logs = localGeogit.geogit.command(LogOp.class).call();
        assertNotNull(logs);
        assertFalse(logs.hasNext());

        // clone from the remote
        CloneOp clone = clone();
        clone.setDepth(2);
        clone.setRepositoryURL(remoteGeogit.envHome.getCanonicalPath()).call();

        // Make sure the local repository got only 2 commits
        logs = localGeogit.geogit.command(LogOp.class).call();
        logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(2, logged.size());
        assertEquals(expected.get(0), logged.get(0));
        assertEquals(expected.get(1), logged.get(1));
    }

    @Test
    public void testCloneRepoWithBranches() throws Exception {
        // Commit several features to the remote

        LinkedList<RevCommit> expectedMaster = new LinkedList<RevCommit>();
        LinkedList<RevCommit> expectedBranch = new LinkedList<RevCommit>();

        insertAndAdd(remoteGeogit.geogit, points1);
        RevCommit commit = remoteGeogit.geogit.command(CommitOp.class).call();
        expectedMaster.addFirst(commit);
        expectedBranch.addFirst(commit);

        // Create and checkout branch1
        remoteGeogit.geogit.command(BranchCreateOp.class).setAutoCheckout(true).setName("Branch1")
                .call();

        // Commit some changes to branch1
        insertAndAdd(remoteGeogit.geogit, points2);
        commit = remoteGeogit.geogit.command(CommitOp.class).call();
        expectedBranch.addFirst(commit);

        insertAndAdd(remoteGeogit.geogit, points3);
        commit = remoteGeogit.geogit.command(CommitOp.class).call();
        expectedBranch.addFirst(commit);

        // Make sure Branch1 has all of the commits
        Iterator<RevCommit> logs = remoteGeogit.geogit.command(LogOp.class).call();
        List<RevCommit> logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expectedBranch, logged);

        // Checkout master and commit some changes
        remoteGeogit.geogit.command(CheckoutOp.class).setSource("master").call();

        insertAndAdd(remoteGeogit.geogit, lines1);
        commit = remoteGeogit.geogit.command(CommitOp.class).call();
        expectedMaster.addFirst(commit);

        insertAndAdd(remoteGeogit.geogit, lines2);
        commit = remoteGeogit.geogit.command(CommitOp.class).call();
        expectedMaster.addFirst(commit);

        // Make sure master has all of the commits
        logs = remoteGeogit.geogit.command(LogOp.class).call();
        logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expectedMaster, logged);

        // Make sure the local repository has no commits prior to clone
        logs = localGeogit.geogit.command(LogOp.class).call();
        assertNotNull(logs);
        assertFalse(logs.hasNext());

        // clone from the remote
        CloneOp clone = clone();
        clone.setRepositoryURL(remoteGeogit.envHome.getCanonicalPath()).setBranch("Branch1").call();

        // Make sure the local repository got all of the commits
        logs = localGeogit.geogit.command(LogOp.class).call();
        logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expectedBranch, logged);

        // Make sure the local master matches the remote
        localGeogit.geogit.command(CheckoutOp.class).setSource("master").call();

        logs = localGeogit.geogit.command(LogOp.class).call();
        logged = new ArrayList<RevCommit>();
        for (; logs.hasNext();) {
            logged.add(logs.next());
        }

        assertEquals(expectedMaster, logged);
    }

    @Test
    public void testCloneEmptyRepo() throws Exception {
        CloneOp clone = clone();
        clone.setRepositoryURL(remoteGeogit.envHome.getCanonicalPath()).call();
    }

    @Test
    public void testCloneNoRepoSpecified() throws Exception {
        CloneOp clone = clone();
        exception.expect(IllegalArgumentException.class);
        clone.call();
    }

    @Test
    public void testCloneEmptyRepoString() throws Exception {
        CloneOp clone = clone();
        exception.expect(IllegalArgumentException.class);
        clone.setRepositoryURL("").call();
    }

}
