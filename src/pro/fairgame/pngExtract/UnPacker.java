// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.util.zip.DataFormatException;
import java.io.IOException;
import pngExtract.functions.Split;
import pngExtract.functions.Merge;
import pngExtract.functions.Pack;
import java.awt.Component;
import javax.swing.JOptionPane;
import pngExtract.functions.Unpack;
import pngExtract.functions.PacketTree;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class UnPacker
{
    private static final String packDescription = ".xml file (all [main].png files required)";
    private static final String unpackDescription = ".pack file required";
    private static final String splitDescription = ".xml file (all [main].png files required)";
    private static final String mergeDescription = ".xml file (all   [sub].png files required)";
    private static JFrame frame;
    private static JPanel panel;
    private static JButton chooseFile;
    private static JButton xmlViewer;
    private static JFileChooser fc;
    private static JButton run;
    private static ActionButler actionButler;
    private static CheckboxGroup functionCheckboxGroup;
    private static Checkbox pack;
    private static Checkbox unpack;
    private static Checkbox split;
    private static Checkbox merge;
    private static String fileName;
    private static String xmlFileName;
    
    public static void main(final String[] args) {
        UnPacker.actionButler = new ActionButler(null);
        setupFrame();
    }
    
    private static boolean startFunction(final String function, final String file) {
        try {
            switch (function) {
                case "xmlViewer": {
                    PacketTree.createXMLViewer(file);
                    PacketTree.Tree2Object();
                    return false;
                }
                case "unpack": {
                    if (file.contains(".pack") && file.length() > 5) {
                        UnPacker.xmlFileName = Unpack.unpack(file);
                        return true;
                    }
                    JOptionPane.showMessageDialog(null, String.format(".pack file expected", new Object[0]), "", 1);
                    return false;
                }
                case "pack": {
                    if (file.contains(".xml") && file.length() > 4) {
                        Pack.pack(file);
                        return true;
                    }
                    JOptionPane.showMessageDialog(null, String.format(".xml file expected", new Object[0]), "", 1);
                    return false;
                }
                case "merge": {
                    Merge.merge(file);
                    JOptionPane.showMessageDialog(null, String.format(file, new Object[0]), "", 1);
                    return false;
                }
                case "split": {
                    if (file.contains(".xml") && file.length() > 5) {
                        Split.split(file);
                        return true;
                    }
                    JOptionPane.showMessageDialog(null, String.format(".xml file expected", new Object[0]), "", 1);
                    return false;
                }
                default:
                    break;
            }
            JOptionPane.showMessageDialog(null, String.format("Something went wrong.\nYou wanted to \"%s\" with the file:\n%s", function, file), "", 1);
        }
        catch (final IOException | DataFormatException e) {
            JOptionPane.showMessageDialog(null, String.format("Exception:\n%s", e.toString()), "", 1);
        }
        return false;
    }
    
    private static void setupFrame() {
        (UnPacker.frame = new JFrame("PZ UnPacker")).setLocation(300, 300);
        UnPacker.frame.setDefaultCloseOperation(3);
        UnPacker.frame.setResizable(false);
        UnPacker.frame.setLayout(new BorderLayout(5, 5));
        UnPacker.panel = new JPanel();
        setupPanel();
        UnPacker.frame.getContentPane().add(UnPacker.panel);
        UnPacker.frame.setPreferredSize(new Dimension(330, 200));
        UnPacker.frame.pack();
        UnPacker.frame.setVisible(true);
    }
    
    private static void setupPanel() {
        UnPacker.panel.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        UnPacker.functionCheckboxGroup = new CheckboxGroup();
        (UnPacker.fc = new JFileChooser()).setCurrentDirectory(new File(new File(".").getAbsolutePath()));
        (UnPacker.chooseFile = new JButton("Choose File")).addActionListener(UnPacker.actionButler);
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 0;
        UnPacker.panel.add(UnPacker.chooseFile, c);
        c.fill = 2;
        c.gridx = 2;
        c.gridy = 0;
        UnPacker.panel.add(new JLabel("                                          "), c);
        (UnPacker.xmlViewer = new JButton("viewXML")).setEnabled(false);
        UnPacker.xmlViewer.addActionListener(UnPacker.actionButler);
        c.fill = 2;
        c.anchor = 20;
        c.gridx = 3;
        c.gridy = 0;
        UnPacker.panel.add(UnPacker.xmlViewer, c);
        UnPacker.pack = new Checkbox("pack", UnPacker.functionCheckboxGroup, false);
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 1;
        UnPacker.panel.add(UnPacker.pack, c);
        JLabel textLabel = new JLabel(".xml file (all [main].png files required)");
        c.fill = 2;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;
        UnPacker.panel.add(textLabel, c);
        UnPacker.unpack = new Checkbox("unpack", UnPacker.functionCheckboxGroup, true);
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 2;
        UnPacker.panel.add(UnPacker.unpack, c);
        textLabel = new JLabel(".pack file required");
        c.fill = 2;
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 2;
        UnPacker.panel.add(textLabel, c);
        UnPacker.split = new Checkbox("split", UnPacker.functionCheckboxGroup, false);
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 3;
        UnPacker.panel.add(UnPacker.split, c);
        textLabel = new JLabel(".xml file (all [main].png files required)");
        c.fill = 2;
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 2;
        UnPacker.panel.add(textLabel, c);
        UnPacker.merge = new Checkbox("merge", UnPacker.functionCheckboxGroup, false);
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 4;
        UnPacker.panel.add(UnPacker.merge, c);
        textLabel = new JLabel(".xml file (all   [sub].png files required)");
        c.fill = 2;
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 2;
        UnPacker.panel.add(textLabel, c);
        (UnPacker.run = new JButton("Run")).setEnabled(false);
        UnPacker.run.addActionListener(UnPacker.actionButler);
        c.fill = 2;
        c.anchor = 20;
        c.gridx = 3;
        c.gridy = 5;
        UnPacker.panel.add(UnPacker.run, c);
    }
    
    static /* synthetic */ void access$2(final String fileName) {
        UnPacker.fileName = fileName;
    }
    
    static /* synthetic */ void access$4(final String xmlFileName) {
        UnPacker.xmlFileName = xmlFileName;
    }
    
    private static class ActionButler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == UnPacker.chooseFile) {
                final int returnVal = UnPacker.fc.showOpenDialog(null);
                if (returnVal == 0) {
                    UnPacker.access$2(UnPacker.fc.getSelectedFile().getAbsolutePath());
                    if (UnPacker.fileName.contains(".xml")) {
                        UnPacker.access$4(UnPacker.fileName);
                        UnPacker.xmlViewer.setEnabled(true);
                    }
                    else {
                        UnPacker.access$4(null);
                        UnPacker.xmlViewer.setEnabled(false);
                    }
                    UnPacker.run.setEnabled(true);
                }
            }
            if (e.getSource() == UnPacker.run) {
                UnPacker.run.setEnabled(false);
                UnPacker.xmlViewer.setEnabled(false);
                UnPacker.panel.setCursor(Cursor.getPredefinedCursor(3));
                final boolean success = startFunction(UnPacker.functionCheckboxGroup.getSelectedCheckbox().getLabel(), UnPacker.fileName);
                UnPacker.run.setEnabled(true);
                UnPacker.xmlViewer.setEnabled(true);
                UnPacker.panel.setCursor(null);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Process successfully finished!", "", 1);
                }
            }
            if (e.getSource() == UnPacker.xmlViewer) {
                UnPacker.run.setEnabled(false);
                UnPacker.xmlViewer.setEnabled(false);
                UnPacker.panel.setCursor(Cursor.getPredefinedCursor(3));
                startFunction("xmlViewer", UnPacker.xmlFileName);
                UnPacker.run.setEnabled(true);
                UnPacker.xmlViewer.setEnabled(true);
                UnPacker.panel.setCursor(null);
            }
        }
    }
}
