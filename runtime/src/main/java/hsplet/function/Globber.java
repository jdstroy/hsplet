/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.function;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Filename globber, based on Win32 globbing style.
 * @author jdstroy
 */
public class Globber implements FilenameFilter {

    private String mask;
    private int mode;
    private Pattern regex;

    private enum SpecialCases {

        StarDot, // all files with leading "." or with no "."
        DotStar, // all files with leading "."
        AllFiles, // *.* - all files
        Dot, // ?
        Other, // 
    }
    private SpecialCases dotPosition;

    /**
     * Creates a new Globber, which uses Win32-style globbing on files based on 
     * a mask and these modes:
     * <ul>
     * <li>All files: 0</li>
     * <li>All files except the directory: 1</li>
     * <li>All files except the hidden system attribute: 2</li>
     * <li>All files of the non-attribute system hidden attribute directory: 3</li>
     * <li>Only directory: 5</li>
     * <li>Only hidden system file attribute: 6</li>
     * <li>Only attribute file system and hidden attribute directory: 7</li>
     * </ul>
     */
    
    public Globber(String mask, int mode) {
        this.mask = mask;
        this.mode = mode;
        if (mask.equals("*.*")) {
            dotPosition = SpecialCases.AllFiles;
        } else if (mask.equals("*.")) {
            dotPosition = SpecialCases.StarDot;
        } else if (mask.equals(".*")) {
            dotPosition = SpecialCases.DotStar;
        } else if (mask.equals(".")) {
            dotPosition = SpecialCases.Dot;
        } else {
            dotPosition = SpecialCases.Other;
        }

        this.regex = Pattern.compile(mask.replace(".", Pattern.quote(".")).replace("*", ".*").replace("?", ".?"));

    }

    /** 
     * Handle special cases with *, *., and .*
     */
    private boolean matchesName(String name) {
        switch (dotPosition) {
            case AllFiles:
                return true;
            case DotStar:
                return name.startsWith(".");
            case StarDot:
                return name.startsWith(".") || !name.contains(".");
            case Other:
            case Dot:
            default:
                return regex.matcher(name).matches();
        }

    }

    /**
     * Checks if the file is accepted by this filter, where dir is the parent 
     * directory and name is the filename.
     */
    @Override
    public boolean accept(File dir, String name) {
        if (!matchesName(name)) {
            return false;
        }
        switch (mode) {
            case 0:
                return true;
            case 1:
                return !new File(dir, name).isDirectory();
            case 2:
                return !new File(dir, name).isHidden();
            case 3:
                return !new File(dir, name).isHidden() && !new File(dir, name).isDirectory();
            case 5:
                return new File(dir, name).isDirectory();
            case 6:
                return new File(dir, name).isHidden();
            case 7:
                return new File(dir, name).isHidden() && new File(dir, name).isDirectory();
            default:
                throw new IllegalArgumentException("Incompatible mode requested.");
        }
    }
}
