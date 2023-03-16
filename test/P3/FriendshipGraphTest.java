package P3;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class FriendshipGraphTest {

    @Test
    public void addVertex() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        graph.addVertex(rachel);
        assertEquals(true,graph.getFriend().containsKey(rachel));
        assertEquals(false,graph.getFriend().containsKey(ross));
    }

    @Test
    public void addEdge() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        assertEquals(true,graph.getFriend().get(rachel).contains(ross));
        assertEquals(true,graph.getFriend().get(ross).contains(rachel));
    }

    @Test
    public void getDistance() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        assertEquals(1,graph.getDistance(rachel, ross));
        assertEquals(2,graph.getDistance(rachel, ben));
        assertEquals(-1,graph.getDistance(ben,  kramer));
        assertEquals(0,graph.getDistance(rachel, rachel));
    }
}