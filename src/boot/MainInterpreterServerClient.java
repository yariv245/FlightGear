package boot;

import servers.MyInterpreter;

import java.io.IOException;

public class MainInterpreterServerClient {

    public static void main(String[] args) throws IOException {

        int port = 5400;
        String ip = "127.0.0.1";
//        MyInterpreter.startServer(port +3); //GUI server -  port 5401
        MyInterpreter.startServer(port); // FlightGear live data server port - 5400
        MyInterpreter.startClient(ip,port+1); // connect interpreter to FlightGear server

//        CalcPathServer.runServer(port+2);

//        int port = 5400;
//        Simulator sim = new Simulator(port); // sim_client on port+1, sim_server on port
//
//        String[] connectString = {
//                "openDataServer " + (port + 1) + " 10",
//                "connect 127.0.0.1 " + port,
//        };
//
//        MyInterpreter.interpret(connectString);


//        int rand = r.nextInt(1000);
//
//        String[] test1 = {
//                "return " + rand + " * 5 - (8+2)"
//        };
//        System.out.println("Start test1");
//        if (MyInterpreter.interpret(test1) != rand * 5 - (8 + 2))
//            System.out.println("failed test1 (-20)");
//        System.out.println("Finish test1");
//
//        String[] test2 = {
//                "var x",
//                "x=" + rand,
//                "var y=x+3",
//                "return y"
//        };
//        System.out.println("Start test2");
//        if (MyInterpreter.interpret(test2) != rand + 3)
//            System.out.println("failed test2 (-20)");
//        System.out.println("Finish test2");
//
//        String[] test3 = {
//                "openDataServer " + (port + 1) + " 10",
//                "connect 127.0.0.1 " + port,
//                "var x",
//                "x = bind simX",
//                "var y = bind simX",
//                "x = " + rand * 2,
//                "disconnect",
//                "return y"
//        };
//
//        System.out.println("Start test3");
//        if (MyInterpreter.interpret(test3) != rand * 2)
//            System.out.println("failed test3 (-20)");
//        System.out.println("Finish test3");
//
//        String[] test4 = {
//                "openDataServer " + (port + 1) + " 10",
//                "connect 127.0.0.1 " + port,
//                "var x = bind simX",
//                "var y = bind simY",
//                "var z = bind simZ",
//                "x = " + rand * 2,
//                "disconnect",
//                "return x+y*z"
//        };
//        System.out.println("Start test4");
//        System.out.println("Random*2: " + rand * 2);
//        int result = 0;
//        if ((result = MyInterpreter.interpret(test4)) != sim.simX + sim.simY * sim.simZ)
//            System.out.println("failed test4 (-20) my result:" + result + " target result" + (sim.simX + sim.simY * sim.simZ));
//        System.out.println("Finish test4");
//
//        String[] test5 = {
//                "var x = 0",
//                "var y = " + rand,
//                "while x < 5 {",
//                "	y = y + 2",
//                "	x = x + 1",
//                "}",
//                "return y"
//        };
//
//        if (MyInterpreter.interpret(test5) != rand + 2 * 5)
//            System.out.println("failed test5 (-20)");
//
//        sim.close();
//        System.out.println("done");
    }

}
