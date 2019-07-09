package com.salesforce.tests.dependency;

import java.util.*;

/**
 * The entry point for the Test program
 */
public class Main {

    public static void main(String[] args) {

        //read input from stdin
        Scanner scan = new Scanner(System.in);
        Map<String, List<String>> dependencyMap = new HashMap<>();
        Map<String, Set<String>> reverseDependencyMap = new HashMap<>();
        Set<String> installedUtilities = new LinkedHashSet<>();

        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }

            //the END command to stop the program
            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }

            // Listing all installed Utilities
            if("LIST".equals(line)){
                System.out.println(line);
                for(String utility: installedUtilities){
                    System.out.println(utility);
                }
            }

            // Storing Dependencies in a HashMap (telnet -> List(tcpip,netcard))
            // Also Storing the reverse mapping of dependencies in a HashMap (tcpip -> List(telnet))  and (netcard -> List(telnet)) :  This is Useful for REMOVE
            if(line.startsWith("DEPEND")){
                System.out.println(line);
                List<String> dependencies = Arrays.asList(line.split(" "));
                if(dependencies.size()< 3){
                    System.out.println("DEPEND should atleast have 2 modules");
                    break;
                }
                List dependencyList = new ArrayList<String>();

                for(int i = 2; i < dependencies.size(); i ++){
                    if(dependencyMap.get(dependencies.get(i))!= null && dependencyMap.get(dependencies.get(i)).contains(dependencies.get(1))){
                        System.out.println(dependencies.get(i)+" depends on "+dependencies.get(1)+", ignoring command");
                        continue;
                    }
                    dependencyList.add(dependencies.get(i));
                    Set<String> reverseDependencyList = new HashSet<>();
                    if(reverseDependencyMap.size() > 0 &&  reverseDependencyMap.get(dependencies.get(i))!=null && reverseDependencyMap.get(dependencies.get(i)).size() > 0){
                        reverseDependencyList.addAll(reverseDependencyMap.get(dependencies.get(i)));
                    }

                    reverseDependencyList.add(dependencies.get(1));
                    reverseDependencyMap.put(dependencies.get(i), reverseDependencyList);
                }

                if(dependencyList.size() > 0){
                    dependencyMap.put(dependencies.get(1), dependencyList);
                }
            }

            //Storing installed Utilities in a HashSet (telnet)
            if(line.startsWith("INSTALL")){
                System.out.println(line);
                List<String> installs = Arrays.asList(line.split(" "));
                if(installs.size() < 2){
                    System.out.println("INSTALL should atleast have 1 module");
                    break;
                }
                String utility = installs.get(1);

                List<String> dependencies = dependencyMap.get(utility);
                if(dependencies!=null){
                    for(String dependentUtility: dependencies){
                        if(!installedUtilities.contains(dependentUtility)){
                            System.out.println("Installing "+dependentUtility);
                            installedUtilities.add(dependentUtility);
                        }
                    }
                }

                if(installedUtilities.contains(utility)){
                    System.out.println(utility+" is already installed");

                }else{
                    System.out.println("Installing "+utility);
                    installedUtilities.add(utility);
                }
            }

            // Removing the Utilities from installedSet and also making sure we remove the dependencies if they are alone. (ReverseMap is useful here)
            if(line.startsWith("REMOVE")){
                System.out.println(line);
                List<String> removes = Arrays.asList(line.split(" "));
                if(removes.size() < 2){
                    System.out.println("REMOVE should atleast have 1 module");
                    break;
                }
                String removeUtility = removes.get(1);

                if(reverseDependencyMap.get(removeUtility)!=null && reverseDependencyMap.get(removeUtility).size() > 0){
                    System.out.println(removeUtility+" is still needed");
                }else{
                    if(!installedUtilities.contains(removeUtility)){
                        System.out.println(removeUtility+" is not installed");
                    }else{
                        System.out.println("Removing "+removeUtility);
                        installedUtilities.remove(removeUtility);

                        List<String> dependencies = dependencyMap.get(removeUtility);
                        for(String dependency: dependencies){
                            reverseDependencyMap.get(dependency).remove(removeUtility);
                            if(reverseDependencyMap.get(dependency)!=null && reverseDependencyMap.get(dependency).size() == 0){
                                System.out.println("Removing "+dependency);
                                installedUtilities.remove(dependency);
                            }
                        }
                    }
                }
            }
        }
    }
}