package P3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FriendshipGraph {
    private HashMap<Person, HashSet<Person>> friendship; //用HashMap保存关系网

    public FriendshipGraph() {
        friendship = new HashMap<Person, HashSet<Person>>();
    }

    public HashMap<Person, HashSet<Person>> getFriend() {
        HashMap<Person, HashSet<Person>> f = friendship;
        return f;
    }

    public void addVertex(Person p) {
        if (!friendship.containsKey(p)) {
            friendship.put(p, new HashSet<Person>());
        } else {
            System.out.println("该人物在关系网中已经存在");
            System.exit(0);
        }
    }

    public void addEdge(Person A, Person B) {
        if (!friendship.containsKey(A)) {
            System.out.println("该人物" + A + "还未加入社交网络");
            System.exit(0);
            return;
        }
        if (!friendship.containsKey(B)) {
            System.out.println("该人物" + B + "还未加入社交网络");
            System.exit(0);
            return;
        }
        friendship.get(A).add(B);
    }

    public int getDistance(Person p1, Person p2) {
        if (!friendship.containsKey(p1)) {
            System.out.println("该人物" + p1 + "还未加入社交网络");
            System.exit(0);
            return -1;
        }
        if (!friendship.containsKey(p2)) {
            System.out.println("该人物" + p2 + "还未加入社交网络");
            System.exit(0);
            return -2;
        }
        int distance = 0; //距离
        HashSet<Person> visited = new HashSet<>(); //记录访问过的顶点
        HashMap<Integer, HashSet<Person>> map = new HashMap<>(); //记录距离p1一定距离的顶点集
        map.put(0, new HashSet<Person>());
        map.get(0).add(p1);
        do {
            if (map.get(distance).contains(p2)) {
                return distance;
            } else {
                visited.addAll(map.get(distance));
                map.put(distance + 1, new HashSet<Person>());
                for (Person i : map.get(distance)) {
                    if (friendship.get(i).size() != 0) {
                        for (Person p : friendship.get(i)) {
                            if (!visited.contains(p)) {
                                map.get(distance + 1).add(p);
                            }
                        }
                    }
                }
            }
            distance++;
        } while (map.get(distance).size() != 0);
        return -1;
    }

    public static void main(String[] args) {
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
        System.out.println(graph.getDistance(rachel, ross));
//should print 1
        System.out.println(graph.getDistance(rachel, ben));
//should print 2
        System.out.println(graph.getDistance(rachel, rachel));
//should print 0
        System.out.println(graph.getDistance(rachel, kramer));
//should print -1
    }

}
