// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import java.beans.XMLEncoder;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import pngExtract.components.Entry;
import pngExtract.components.Frame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.LayoutManager;
import java.awt.FlowLayout;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Component;
import java.io.InputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import pngExtract.components.Packet;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTree;

public class PacketTree extends JTree
{
    private static final long serialVersionUID = -7182525389515034316L;
    private static String file;
    private static JFrame frame;
    private static JPanel panel;
    private static JScrollPane scrollPane;
    private static PacketTree tree;
    private static JButton reload;
    private static JButton expandAll;
    private static JButton collapseAll;
    private static JButton save;
    private static ActionButler actionButler;
    private static Packet pack;
    
    public static void createXMLViewer(final String fileName) throws IOException {
        PacketTree.file = fileName;
        final FileInputStream os = new FileInputStream(PacketTree.file);
        final XMLDecoder decoder = new XMLDecoder(os);
        PacketTree.pack = (Packet)decoder.readObject();
        decoder.close();
        PacketTree.actionButler = new ActionButler(null);
        (PacketTree.frame = new JFrame(String.format("XMLTreeView: [ %s ]", PacketTree.file.substring(PacketTree.file.lastIndexOf("\\") + 1, PacketTree.file.length())))).setLocation(100, 100);
        (PacketTree.tree = new PacketTree(PacketTree.pack)).setEditable(true);
        PacketTree.frame.setDefaultCloseOperation(2);
        PacketTree.panel = new JPanel();
        setupPanel();
        PacketTree.frame.getContentPane().add(PacketTree.panel);
        PacketTree.frame.setPreferredSize(new Dimension(500, 600));
        PacketTree.frame.setResizable(false);
        PacketTree.frame.setVisible(true);
        PacketTree.frame.pack();
    }
    
    private static void setupPanel() {
        final JPanel inlinePanel = new JPanel();
        inlinePanel.setLayout(new FlowLayout(1, 40, 0));
        (PacketTree.reload = new JButton("Reload")).addActionListener(PacketTree.actionButler);
        inlinePanel.add(PacketTree.reload);
        (PacketTree.expandAll = new JButton("Expand All")).addActionListener(PacketTree.actionButler);
        inlinePanel.add(PacketTree.expandAll);
        (PacketTree.collapseAll = new JButton("Collapse All")).addActionListener(PacketTree.actionButler);
        inlinePanel.add(PacketTree.collapseAll);
        (PacketTree.save = new JButton("Save")).addActionListener(PacketTree.actionButler);
        inlinePanel.add(PacketTree.save);
        PacketTree.scrollPane = new JScrollPane(PacketTree.tree);
        PacketTree.panel.setLayout(new BorderLayout());
        PacketTree.panel.add(inlinePanel, "First");
        PacketTree.panel.add(PacketTree.scrollPane, "Center");
    }
    
    PacketTree(final Packet packet) {
        super(makeRootNode(packet));
    }
    
    public static Packet Tree2Object() {
        final Packet packet = new Packet();
        PacketTree.tree.setSelectionPath(PacketTree.tree.getPathForLocation(0, 0));
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)PacketTree.tree.getLastSelectedPathComponent();
        packet.setPacketName(root.getChildAt(0).getChildAt(0).toString());
        packet.setNumImages(Integer.valueOf(root.getChildAt(1).getChildAt(0).toString()));
        final int numImages = Integer.valueOf(root.getChildAt(1).getChildAt(0).toString());
        final Frame[] frames = new Frame[numImages];
        for (int i = 0; i < numImages; ++i) {
            if (i < root.getChildAt(2).getChildCount()) {
                frames[i] = new Frame();
                final TreeNode frameNode = root.getChildAt(2).getChildAt(i);
                frames[i].setFileNameSize(Integer.valueOf(frameNode.getChildAt(0).getChildAt(0).toString()));
                frames[i].setFilename(frameNode.getChildAt(1).getChildAt(0).toString());
                final int numFrameEntries = Integer.valueOf(frameNode.getChildAt(2).getChildAt(0).toString());
                frames[i].setNumFrameEntries(numFrameEntries);
                frames[i].setBooleanX(Boolean.valueOf(frameNode.getChildAt(3).getChildAt(0).toString()));
                final Entry[] entries = new Entry[numFrameEntries];
                for (int j = 0; j < numFrameEntries; ++j) {
                    entries[j] = new Entry();
                    final TreeNode entryNode = frameNode.getChildAt(4).getChildAt(j);
                    entries[j].setNameSize(Integer.valueOf(entryNode.getChildAt(0).getChildAt(0).toString()));
                    entries[j].setName(entryNode.getChildAt(1).getChildAt(0).toString());
                    entries[j].setXCoord(Integer.valueOf(entryNode.getChildAt(2).getChildAt(0).toString()));
                    entries[j].setYCoord(Integer.valueOf(entryNode.getChildAt(3).getChildAt(0).toString()));
                    entries[j].setWidth(Integer.valueOf(entryNode.getChildAt(4).getChildAt(0).toString()));
                    entries[j].setHeight(Integer.valueOf(entryNode.getChildAt(5).getChildAt(0).toString()));
                    entries[j].setXOffset(Integer.valueOf(entryNode.getChildAt(6).getChildAt(0).toString()));
                    entries[j].setYOffset(Integer.valueOf(entryNode.getChildAt(7).getChildAt(0).toString()));
                    entries[j].setActualWidth(Integer.valueOf(entryNode.getChildAt(8).getChildAt(0).toString()));
                    entries[j].setActualHeight(Integer.valueOf(entryNode.getChildAt(9).getChildAt(0).toString()));
                }
                frames[i].setFrameEntries(entries);
            }
        }
        packet.setFrames(frames);
        return packet;
    }
    
    private static DefaultTreeModel makeRootNode(final Packet packet) {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Packet: ( " + packet.getPacketName() + " )");
        final DefaultMutableTreeNode packetNameNode = new DefaultMutableTreeNode("packetName");
        root.add(packetNameNode);
        packetNameNode.add(new DefaultMutableTreeNode(packet.getPacketName()));
        final DefaultMutableTreeNode numImagesNode = new DefaultMutableTreeNode("numImages");
        root.add(numImagesNode);
        numImagesNode.add(new DefaultMutableTreeNode(packet.getNumImages()));
        final DefaultMutableTreeNode framesNode = new DefaultMutableTreeNode("frames");
        root.add(framesNode);
        final Frame[] frames = packet.getFrames();
        if (frames != null) {
            final DefaultMutableTreeNode[] frameNode = new DefaultMutableTreeNode[frames.length];
            for (int i = 0; i < packet.getNumImages(); ++i) {
                if (frames[i] != null) {
                    framesNode.add(frameNode[i] = new DefaultMutableTreeNode("Frame: ( " + frames[i].getFilename() + " )"));
                    final DefaultMutableTreeNode fileNameSizeNode = new DefaultMutableTreeNode("fileNameSize");
                    frameNode[i].add(fileNameSizeNode);
                    fileNameSizeNode.add(new DefaultMutableTreeNode(frames[i].getFileNameSize()));
                    final DefaultMutableTreeNode fileNameNode = new DefaultMutableTreeNode("fileName");
                    frameNode[i].add(fileNameNode);
                    fileNameNode.add(new DefaultMutableTreeNode(frames[i].getFilename()));
                    final DefaultMutableTreeNode numFrameEntriesNode = new DefaultMutableTreeNode("numFrameEntries");
                    frameNode[i].add(numFrameEntriesNode);
                    numFrameEntriesNode.add(new DefaultMutableTreeNode(frames[i].getNumFrameEntries()));
                    final DefaultMutableTreeNode booleanNode = new DefaultMutableTreeNode("boolean");
                    frameNode[i].add(booleanNode);
                    booleanNode.add(new DefaultMutableTreeNode(frames[i].getBooleanX()));
                    final DefaultMutableTreeNode entriesNode = new DefaultMutableTreeNode("frameEntries");
                    frameNode[i].add(entriesNode);
                    final Entry[] entries = frames[i].getFrameEntries();
                    final DefaultMutableTreeNode[] entryNode = new DefaultMutableTreeNode[entries.length];
                    for (int j = 0; j < frames[i].getNumFrameEntries(); ++j) {
                        entriesNode.add(entryNode[j] = new DefaultMutableTreeNode("Entry: ( " + entries[j].getName() + " )"));
                        final DefaultMutableTreeNode nameSizeNode = new DefaultMutableTreeNode("nameSize");
                        entryNode[j].add(nameSizeNode);
                        nameSizeNode.add(new DefaultMutableTreeNode(entries[j].getNameSize()));
                        final DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode("name");
                        entryNode[j].add(nameNode);
                        nameNode.add(new DefaultMutableTreeNode(entries[j].getName()));
                        final DefaultMutableTreeNode xCoordNode = new DefaultMutableTreeNode("xCoord");
                        entryNode[j].add(xCoordNode);
                        xCoordNode.add(new DefaultMutableTreeNode(entries[j].getXCoord()));
                        final DefaultMutableTreeNode yCoordNode = new DefaultMutableTreeNode("yCoord");
                        entryNode[j].add(yCoordNode);
                        yCoordNode.add(new DefaultMutableTreeNode(entries[j].getYCoord()));
                        final DefaultMutableTreeNode widthNode = new DefaultMutableTreeNode("width");
                        entryNode[j].add(widthNode);
                        widthNode.add(new DefaultMutableTreeNode(entries[j].getWidth()));
                        final DefaultMutableTreeNode heightNode = new DefaultMutableTreeNode("height");
                        entryNode[j].add(heightNode);
                        heightNode.add(new DefaultMutableTreeNode(entries[j].getHeight()));
                        final DefaultMutableTreeNode xOffsetNode = new DefaultMutableTreeNode("xOffset");
                        entryNode[j].add(xOffsetNode);
                        xOffsetNode.add(new DefaultMutableTreeNode(entries[j].getXOffset()));
                        final DefaultMutableTreeNode yOffsetNode = new DefaultMutableTreeNode("yOffset");
                        entryNode[j].add(yOffsetNode);
                        yOffsetNode.add(new DefaultMutableTreeNode(entries[j].getYOffset()));
                        final DefaultMutableTreeNode actualWidthNode = new DefaultMutableTreeNode("actualWidth");
                        entryNode[j].add(actualWidthNode);
                        actualWidthNode.add(new DefaultMutableTreeNode(entries[j].getActualWidth()));
                        final DefaultMutableTreeNode actualHeightNode = new DefaultMutableTreeNode("actualHeight");
                        entryNode[j].add(actualHeightNode);
                        actualHeightNode.add(new DefaultMutableTreeNode(entries[j].getActualHeight()));
                    }
                }
            }
        }
        return new DefaultTreeModel(root);
    }
    
    static /* synthetic */ void access$6(final Packet pack) {
        PacketTree.pack = pack;
    }
    
    static /* synthetic */ void access$8(final PacketTree tree) {
        PacketTree.tree = tree;
    }
    
    static /* synthetic */ void access$12(final JScrollPane scrollPane) {
        PacketTree.scrollPane = scrollPane;
    }
    
    private static class ActionButler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == PacketTree.reload) {
                PacketTree.expandAll.setEnabled(false);
                PacketTree.collapseAll.setEnabled(false);
                PacketTree.save.setEnabled(false);
                PacketTree.frame.getContentPane().setCursor(Cursor.getPredefinedCursor(3));
                try {
                    final FileInputStream os = new FileInputStream(PacketTree.file);
                    final XMLDecoder decoder = new XMLDecoder(os);
                    PacketTree.access$6((Packet)decoder.readObject());
                    decoder.close();
                    PacketTree.access$8(new PacketTree(PacketTree.pack));
                    PacketTree.tree.setEditable(true);
                    PacketTree.panel.remove(PacketTree.scrollPane);
                    PacketTree.access$12(new JScrollPane(PacketTree.tree));
                    PacketTree.panel.add(PacketTree.scrollPane, "Center");
                    PacketTree.frame.pack();
                }
                catch (final IOException ex) {
                    ex.printStackTrace();
                }
                PacketTree.expandAll.setEnabled(true);
                PacketTree.collapseAll.setEnabled(true);
                PacketTree.save.setEnabled(true);
                PacketTree.frame.getContentPane().setCursor(null);
            }
            if (e.getSource() == PacketTree.expandAll) {
                PacketTree.collapseAll.setEnabled(false);
                PacketTree.reload.setEnabled(false);
                PacketTree.save.setEnabled(false);
                PacketTree.frame.getContentPane().setCursor(Cursor.getPredefinedCursor(3));
                for (int i = 0; i < PacketTree.tree.getRowCount(); ++i) {
                    PacketTree.tree.expandRow(i);
                }
                PacketTree.collapseAll.setEnabled(true);
                PacketTree.reload.setEnabled(true);
                PacketTree.save.setEnabled(true);
                PacketTree.frame.getContentPane().setCursor(null);
            }
            if (e.getSource() == PacketTree.collapseAll) {
                PacketTree.expandAll.setEnabled(false);
                PacketTree.reload.setEnabled(false);
                PacketTree.save.setEnabled(false);
                PacketTree.frame.getContentPane().setCursor(Cursor.getPredefinedCursor(3));
                for (int i = 0; i < PacketTree.tree.getRowCount(); ++i) {
                    PacketTree.tree.collapseRow(i);
                }
                PacketTree.expandAll.setEnabled(true);
                PacketTree.reload.setEnabled(true);
                PacketTree.save.setEnabled(true);
                PacketTree.frame.getContentPane().setCursor(null);
            }
            if (e.getSource() == PacketTree.save) {
                PacketTree.collapseAll.setEnabled(false);
                PacketTree.expandAll.setEnabled(false);
                PacketTree.reload.setEnabled(false);
                PacketTree.frame.getContentPane().setCursor(Cursor.getPredefinedCursor(3));
                try {
                    final Packet pack = PacketTree.Tree2Object();
                    final XMLEncoder enc = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(PacketTree.file)));
                    enc.writeObject(pack);
                    enc.close();
                }
                catch (final FileNotFoundException e2) {
                    JOptionPane.showMessageDialog(null, "File could not be saved.", "", 1);
                    e2.printStackTrace();
                }
                PacketTree.collapseAll.setEnabled(true);
                PacketTree.expandAll.setEnabled(true);
                PacketTree.reload.setEnabled(true);
                PacketTree.frame.getContentPane().setCursor(null);
                JOptionPane.showMessageDialog(null, "File successfully saved.", "", 1);
            }
        }
    }
}
