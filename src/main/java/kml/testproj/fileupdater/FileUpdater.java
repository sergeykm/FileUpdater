package kml.testproj.fileupdater;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;


public class FileUpdater{

    public static void main(String[] args){
        new FileUpdater();
    }

    private FileUpdater(){

        final JFrame frame = new JFrame("File Updater");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,8));
        JPanel packControls = new JPanel(new BorderLayout(20,0));

        JPanel inControls = new JPanel(new GridLayout(2,1,0,10));

        JPanel topInControls = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        JLabel descriptionSrc = new JLabel("Source:");
        topInControls.add(descriptionSrc);
        final JTextField srcField = new JTextField(srcDir,25);
        topInControls.add(srcField);
        JButton srcBrowse = new JButton("Browse");
        topInControls.add(srcBrowse);
        inControls.add(topInControls);

        JPanel bottomInControls = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        JLabel descriptionDest = new JLabel("Destination:");
        bottomInControls.add(descriptionDest);
        final JTextField destField = new JTextField(destDir, 25);
        bottomInControls.add(destField);
        JButton destBrowse = new JButton("Browse");
        bottomInControls.add(destBrowse);
        inControls.add(bottomInControls);

        packControls.add(inControls, BorderLayout.CENTER);

        packControls.add(checkButton, BorderLayout.LINE_END);

        controls.add(packControls);
        frame.add(controls, BorderLayout.PAGE_START);

        final JTabbedPane tabbedPane = new JTabbedPane();
        JPanel findingsTab = new JPanel(new BorderLayout());



        final DefaultTableModel model = new DefaultTableModel(null, columnsNames){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return columnIndex == 2;
            }
            @Override
            public Class getColumnClass(int c){
                return getValueAt(0,c).getClass();
            }
        };

        final JTable outputTable = new JTable(model){
            //private static final long serialVersionUID = 1L;
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if(column == 2){
                    c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);
                }
                return c;
            }
        };
        outputTable.getTableHeader().setReorderingAllowed(false);
        outputTable.setCellSelectionEnabled(false);
        outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        setTableColumnsSize(outputTable);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column){
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(value!= null){
                    if (column == 0){
                        setHorizontalAlignment(SwingConstants.CENTER);
                        if(value.equals("N")){
                            cell.setForeground(Color.RED);
                        } else if(value.equals("C")){
                            cell.setForeground(Color.ORANGE);
                        } else if(value.equals("O")){
                            cell.setForeground(Color.GREEN);
                        }
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                        cell.setForeground(table.getForeground());
                    }
                    if(row%2 == 0){
                        cell.setBackground(Color.WHITE);
                    } else {
                        cell.setBackground(Color.LIGHT_GRAY);
                    }
                }
                return cell;
            }
        };
        outputTable.setDefaultRenderer(Object.class, renderer);

        JScrollPane scroll = new JScrollPane(outputTable);
        findingsTab.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        notes.setEnabled(false);
        updateButton.setEnabled(false);
        footer.add(notes);
        footer.add(updateButton);
        findingsTab.add(footer, BorderLayout.PAGE_END);

        tabbedPane.addTab("Output", findingsTab);
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.add(status, BorderLayout.PAGE_END);

        frame.setPreferredSize(preferredFrameSize);
        frame.setMinimumSize(minimumFrameSize);
        frame.pack();
        frame.setVisible(true);
        frame.validate();


        srcField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
            }
            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
        });
        destField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
            }
            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
        });


        final JFileChooser dirChooser = new JFileChooser();
        dirChooser.setAcceptAllFileFilterUsed(false);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        srcBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirChooser.setCurrentDirectory(new File(srcDir));
                int ret = dirChooser.showDialog(frame, "Set source");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    srcDir = dirChooser.getSelectedFile().getPath();
                    srcField.setText(srcDir);
                    model.setDataVector(null, columnsNames);
                    setTableColumnsSize(outputTable);
                    setStatus("Inactive");
                    notes.setEnabled(false);
                    updateButton.setEnabled(false);


                }
            }
        });

        destBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirChooser.setCurrentDirectory(new File(destDir));
                int ret = dirChooser.showDialog(frame, "Set destination");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    destDir = dirChooser.getSelectedFile().getPath();
                    destField.setText(destDir);
                    model.setDataVector(null, columnsNames);
                    setTableColumnsSize(outputTable);
                    setStatus("Inactive");
                    notes.setEnabled(false);
                    updateButton.setEnabled(false);

                }
            }
        });


        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(checkButton.getText().equals(checkOffText)){
                    checkButton.setText(checkOnText);
                    model.setDataVector(null, columnsNames);
                    setTableColumnsSize(outputTable);

                    final Thread plodder = new Thread(){
                        public void run(){
                            try {
                                data = getTableData(srcDir, destDir);
                            } catch (InterruptedException e1) {
                                setStatus("Canceled");
                                checkButton.setEnabled(true);
                                e1.printStackTrace();
                                return;
                            } catch (NoSuchAlgorithmException e1){
                                //
                                e1.printStackTrace();
                            } catch (IOException e1){
                                //
                                e1.printStackTrace();
                            }

                            model.setDataVector (data, columnsNames);
                            setTableColumnsSize(outputTable);
                            if(data.length > 0){
                                updateButton.setEnabled(true);
                                notes.setEnabled(true);
                            }
                            setStatus("Done" + " (total: " +data.length + ")");
                            checkButton.setEnabled(true);
                            checkButton.setText(checkOffText);
                        }
                    };
                    plodder.start();
                    Thread chief = new Thread(){
                        public void run(){
                            setIsCanceled(false);
                            while (!getIsCanceled() && plodder.isAlive()){
                                try {
                                    sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if(plodder.isAlive()){
                                plodder.interrupt();
                            }
                        }
                    };
                    chief.start();
                } else {
                    checkButton.setEnabled(false);
                    setStatus("Cancelling");
                    setIsCanceled(true);
                    checkButton.setText(checkOffText);
                }
            }
        });




        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final LinkedList<String> copiedFilesList = getModifiedFileList(outputTable.getModel());
                if(updateButton.getText().equals("Update")){
                    checkButton.setEnabled(false);
                    updateButton.setText("Cancel");
                    final Thread plodder = new Thread(){
                        public void run(){
                            try {
                                copyFiles(copiedFilesList, srcDir, destDir);
                            } catch (IOException e1) {
                                //
                                e1.printStackTrace();
                            } catch (InterruptedException e1) {
                                setStatus("Canceled");
                                updateButton.setEnabled(true);
                                checkButton.setEnabled(true);
                                updateButton.setText("Update");
                                e1.printStackTrace();
                                return;
                            }
                            int size = copiedFilesList.size();
                            setStatus("Done" + " (" + size + "/" + size +" copied)");
                            checkButton.setEnabled(true);
                            updateButton.setEnabled(true);
                            updateButton.setText("Update");
                        }
                    };
                    plodder.start();
                    Thread chief = new Thread(){
                        public void run(){
                            setIsCanceled(false);
                            while (!getIsCanceled() && plodder.isAlive()){
                                try {
                                    sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if(plodder.isAlive()){
                                plodder.interrupt();
                            }
                        }
                    };
                    chief.start();
                } else {
                    setIsCanceled(true);
                    updateButton.setEnabled(false);
                }
            }
        });

    }

    private void setTableColumnsSize(JTable table){
        TableColumn column;
        column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(20);
        column.setMaxWidth(30);
        column = table.getColumnModel().getColumn(2);
        column.setMinWidth(70);
        column.setMaxWidth(100);
    }

    private Object[][] getTableData(String inputPath, String outputPath)
            throws InterruptedException, NoSuchAlgorithmException, IOException{

        setStatus("Reading source folder");
        LinkedList<File> inputFiles = pathWalker.explorePath(new File(inputPath));
        LinkedList<String> inputRelPath = pathWalker.getRelativePaths(inputFiles, inputPath);
        setStatus("Reading destination folder");
        canceller();
        LinkedList<File> outputFiles = pathWalker.explorePath(new File(outputPath));
        LinkedList<String> outputRelPath = pathWalker.getRelativePaths(outputFiles, outputPath);
        setStatus("Comparing by name");
        canceller();
        LinkedList<String> newFiles = fileComparator.compareByName(inputRelPath, outputRelPath);

        for (String nf: newFiles){
            inputRelPath.remove(nf);
        }
        setStatus("Comparing by hash");
        canceller();
        LinkedList<String> differentFiles = fileComparator.compareByHash(inputRelPath, inputPath, outputPath);

        for(String df: differentFiles){
            inputRelPath.remove(df);
        }
        int nfSize = newFiles.size();
        int dSize = differentFiles.size();
        int oSize = inputRelPath.size();
        Object[][] data = accumulateData(new Object[nfSize + dSize + oSize][3], "N", newFiles, 0);
        data = accumulateData(data, "C", differentFiles, nfSize);
        data = accumulateData(data, "O", inputRelPath, nfSize + dSize);

        return data;
    }

    private Object[][] accumulateData(Object[][] d, String mark, LinkedList<String> someData, int startPosition){
        boolean selected = true;
        if(mark.equals("O")){
            selected = false;
        }
        for(int i = 0; i < someData.size(); i++){
            int n = startPosition + i;
            d[n][0] = mark;
            d[n][1] = someData.get(i);
            d[n][2] = selected;
        }
        return d;
    }

    private LinkedList<String> getModifiedFileList(TableModel tm){
        LinkedList<String> modifiedFiles = new LinkedList<String>();
        int r = tm.getRowCount();
        for(int i = 0; i < r; i++){
            if((Boolean)tm.getValueAt(i,2)){
                modifiedFiles.add((String) tm.getValueAt(i, 1));
            }
        }
        return modifiedFiles;
    }

    private void copyFiles(LinkedList<String> copiedFiles, String sourcePath, String destinationPath)
            throws IOException, InterruptedException{
        int size = copiedFiles.size();
        if(!sourcePath.equals(destinationPath) && (size > 0)){
            for(int i = 0; i < size; i++){
                String element = copiedFiles.get(i);
                Files.copy(Paths.get(sourcePath + element), Paths.get(destinationPath + element),
                        StandardCopyOption.REPLACE_EXISTING);
                if(i%100 == 0){
                    setStatus((i+1) + "/" + size +" copied");
                    canceller();
                }
            }
            setStatus(size + "/" + size +" copied");
        }
    }

    private void setStatus(String s){
        this.status.setText(s);
    }

    private void canceller() throws InterruptedException{
        Thread.sleep(1);
    }

    private Boolean getIsCanceled(){
        return isCanceled;
    }

    private void setIsCanceled(Boolean isCanceled){
        this.isCanceled = isCanceled;
    }

    private Boolean isCanceled;

    final JButton checkButton = new JButton(checkOffText);
    final JButton updateButton = new JButton("Update");
    final JLabel notes = new JLabel("* N - new file, C - changed file, O - old version file.");

    private Object[][] data;
    private static final Object[] columnsNames = {"*","FILE LIST", "SELECT"};
    private static final String checkOffText = "Check";
    private static final String checkOnText = "Cancel";
    private final JLabel status = new JLabel("Inactive", JLabel.RIGHT);

    private static final PathWalker pathWalker = new PathWalker();
    private static final FileComparator fileComparator = new FileComparator();

    private String srcDir = System.getProperty("user.dir");
    private String destDir = System.getProperty("user.dir");

    private static final Dimension preferredFrameSize = new Dimension(660,380);
    private static final Dimension minimumFrameSize = new Dimension(640, 320);

}

