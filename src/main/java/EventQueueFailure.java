import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class EventQueueFailure extends JPanel {
    static JButton anotherButton;
    public EventQueueFailure() {
        add(new JButton(new ButtonAction()));
    anotherButton = new JButton("Threads");
    anotherButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ButtonAction.printAWTThreads("button action");
        }
    });
    
    add(anotherButton);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("EventQueueFailure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EventQueueFailure panel = new EventQueueFailure();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static final class ButtonAction extends AbstractAction {
        public ButtonAction() {
            putValue(Action.NAME, "Push Me");
        }
        
        public void actionPerformed(ActionEvent e) {
        anotherButton.setText("active");
            SimpleProgressDialog dialog = new SimpleProgressDialog();
            dialog.pack();
            dialog.setVisible(true);
            MyEventQueue myEventQueue = new MyEventQueue();
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(myEventQueue);
             for (int i = 0; i < 10; i++) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                EventQueue.invokeLater(new ProgressSetter(dialog, i));
            }
            printAWTThreads("Before close()");
        anotherButton.setText("waiting");
            myEventQueue.close();
            if (printAWTThreads("After close()") > 1) {
                System.err.println("Error: More than one AWT-EventQueue exists.\n");
            } else {
                System.out.println();
            }
        anotherButton.setText("suspend");
            dialog.setVisible(false);
        }
        
        private static List<Thread> getAllThreads() {
            ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
            while (root.getParent() != null) {
                root = root.getParent();
            }
            Thread[] threads = new Thread[500];
            int numThreads = root.enumerate(threads, true);
            List<Thread> list = new LinkedList<Thread>();
            for (int i = 0; i < numThreads; i++) {
                list.add(threads[i]);
            }
            return list;
        }
        
        private static int printAWTThreads(String msg) {
            System.out.println(msg);
            List<Thread> threads = getAllThreads();
            int numAWTThreads = 0;
            for (Thread thread : threads) {
                if (thread.toString().contains("AWT-EventQueue")) {
                    System.out.println("   "+thread.toString()+
                            "  id = "+thread.getId());
                    numAWTThreads++;
                }
            }
            return numAWTThreads;
        }
    }
    
    private static final class MyEventQueue extends EventQueue {
        public MyEventQueue() {
        }
        
        public void close() {
            super.pop();
        }
    }
    
    private static final class SimpleProgressDialog extends JDialog {
        private JProgressBar progressBar;
        private JLabel progressLabel;
        public SimpleProgressDialog() {
            progressBar = new JProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMaximum(10);
            progressLabel = new JLabel("Performing work ...");
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(progressBar, BorderLayout.CENTER);
            panel.add(progressLabel, BorderLayout.SOUTH);
            getContentPane().add(panel);
        }
        
        public void setProgress(int  customer ) {
            progressBar.setValue( customer );
        }
    }
    
    private static final class ProgressSetter implements Runnable {
        private SimpleProgressDialog dialog;
        private int  customer ;
        
        public ProgressSetter(SimpleProgressDialog dialog, int  customer ) {
            this.dialog = dialog;
            this. customer  =  customer ;
        }
        
        public void run() {
            dialog.setProgress( customer );
        }
    }
}