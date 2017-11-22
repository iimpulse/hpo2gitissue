package org.monarchinitiative.hpo2git;

import org.monarchinitiative.hpo2git.erneye.ErnAlyzer;

public class Hpo2GitIssue {
    static String ernPath="/home/robinp/Desktop/HPOstuff/ERNEYE/ERN-EYE_HPOWorkingDocumentWG1Dd20171011.csv";

    public static void main(String[] args) {

//        if (args.length<1) {
//            System.out.println("Pass your github password for posting issues!");
//            String r=Arrays.stream(args).collect(Collectors.joining(":"));
//            System.out.println(String.format("Arguments %s",r ));
//            System.exit(10);
//        }
        ErnAlyzer ern = new ErnAlyzer(ernPath,"");

        ern.output();

    }
}
