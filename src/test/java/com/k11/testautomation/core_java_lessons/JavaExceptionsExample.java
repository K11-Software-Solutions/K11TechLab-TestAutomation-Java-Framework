package com.k11.testautomation.core_java_lessons;

import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Maor on 5/16/2018.
 */

public class JavaExceptionsExample {

    @Test
    // This Exception is raised when a file is not accessible or does not open
    public void FileNotFoundException() {

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream("Maor.txt");
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Exception was caught: " + e);        }
        }

    @Test
    // This Exception is raised when exceptional condition has occurred
    // in an arithmetic operation
    public void ArithmeticException () {

        try
        {
            int a = 0, b = 5, c = b / a;
            System.out.println(c);
        }
        catch(ArithmeticException e)
        {
            System.out.println("Exception was caught: " + e);
        }
    }

    @Test
    // This Exception is raised when an array has been accessed with an illegal index
    public void ArrayIndexoutOfBoundException () {

        // Given an Array of three student marks while
        // Index 0 = 55 & Index 1 = 78 & Index 2 = 89
        int smarks[] = {55, 78, 89};

        try
        {
            int m = smarks[3];
            System.out.println("Mark is " + m);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Exception was caught: " + e);
        }
    }

    @Test
    // This Exception is raised when referring to the members of a null object
    public void NullPointerException () {

        try
        {
            String a = null;
            System.out.println(a.charAt(0));
        }
        catch(NullPointerException e)
        {
            System.out.println("Exception was caught: " + e);
        }
    }

    // It is thrown by String class methods to indicate
    // that an index is either negative than the size of the string
    @Test
    public void StringIndexOutOfBoundsException () {

        try
        {
            String a = "Maor Segev test Application";
            char c = a.charAt(55); // accessing 55th element
            System.out.println(c);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            System.out.println("Exception was caught: " + e);
        }
    }

    // This Exception is raised when a method could not convert
    // a string into a numeric format
    @Test
    public void NumberFormatException () {

        try
        {
            int num = Integer.parseInt ("Maor") ; // Maor is a string and not an int
            System.out.println(num);
        }
        catch(NumberFormatException e)
        {
            System.out.println("Exception was caught: " + e);
        }
    }
}