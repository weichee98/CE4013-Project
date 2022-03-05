package main.java;

import main.java.client.ClientRunner;
import main.java.server.ServerRunner;
import org.apache.commons.cli.*;

import java.util.Objects;

public class Runner {
    private static boolean isClient = true;
    private static String serverIp = "0.0.0.0";
    private static float requestLoss = 0.1F;
    private static float responseLoss = 0.1F;
    private static boolean atMostOnce = true;

    public static void main(String[] args) throws Exception {
        getCommandLineArguments(args);
        if (isClient){
            new ClientRunner(serverIp);
        }
        else {
            new ServerRunner(requestLoss, responseLoss, atMostOnce);
        }
    }

    private static void getCommandLineArguments(String[] args) {
        // define options
        Options options = new Options();
        Option config;

        config = OptionBuilder
                .hasArg()
                .withLongOpt("serverclient")
                .withDescription("Run as server or client")
                .create("sc");
        options.addOption(config);

        config = OptionBuilder
                .hasArg()
                .withLongOpt("serverip")
                .withDescription("Set the server ip address to listen to")
                .create("sip");
        options.addOption(config);

        config = OptionBuilder
                .hasArg()
                .withLongOpt("reqlossrate")
                .withDescription("Set Request loss rate")
                .create("reqr");
        options.addOption(config);

        config = OptionBuilder
                .hasArg()
                .withLongOpt("reslossrate")
                .withDescription("Set response loss rate")
                .create("resr");
        options.addOption(config);

        config = OptionBuilder
                .hasArg()
                .withLongOpt("atmostonce")
                .withDescription("To experiment at most once or at least once")
                .create("amo");
        options.addOption(config);

        // define parser
        CommandLine cmd;
        CommandLineParser parser = new BasicParser();
        HelpFormatter helper = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("sc")) {
                String opt_config = cmd.getOptionValue("serverclient");
                if (Objects.equals(opt_config.toUpperCase(), "S") ||
                        Objects.equals(opt_config.toUpperCase(), "SERVER")) {
                    System.out.println("Running as SERVER");
                    isClient = false;
                }
                else System.out.println("Running as CLIENT");
            }
            if (cmd.hasOption("sip")) {
                String opt_config = cmd.getOptionValue("serverip");
                System.out.println("Client is listening to server at ip: " + opt_config);
                serverIp = opt_config;
            }
            if (cmd.hasOption("reqr")) {
                float opt_config = Float.parseFloat(cmd.getOptionValue("reqlossrate"));
                System.out.println("Request loss rate at server is set to: " + opt_config);
                requestLoss = opt_config;
            }
            if (cmd.hasOption("resr")) {
                float opt_config = Float.parseFloat(cmd.getOptionValue("reslossrate"));
                System.out.println("Response loss rate at server is set to: " + opt_config);
                responseLoss = opt_config;
            }
            if (cmd.hasOption("amo")) {
                String opt_config = cmd.getOptionValue("atmostonce");
                if (opt_config.equalsIgnoreCase("FALSE")) {
                    System.out.println("Running rule: At least once");
                    atMostOnce = false;
                }
                else {
                    System.out.println("Running rule: At most once");
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helper.printHelp("Usage:", options);
            System.exit(0);
        }
    }
}
