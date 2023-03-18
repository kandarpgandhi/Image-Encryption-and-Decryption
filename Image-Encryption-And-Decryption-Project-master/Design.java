import javax.swing.*;       
import java.awt.*;          
import java.io.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
//import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.crypto.spec.SecretKeySpec;


public class Design
{
    private static JFrame frame;
    private static JTextField textField, textField_1, textField_2, textField_3, textField_4, textField_5, textField_6, textField_7;
    private static JFileChooser fileChooser;
    private static File file;
    private static String path, key;
    private static FileInputStream fis, fis1;
    private static FileOutputStream fos, fos1;
    private static byte[] digestdBytes;
    private static int primeNumber, primitiveRoot, privateInt, otherUserPubKey;  //flag for closing event
    private static double pubKey, privateKey;

    public Design()     //Constructor 
    {
        initializeFirst(); 
    }

    public static void openDialog(String check)
    {
        fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        file = fileChooser.getSelectedFile();
        path = file.getAbsolutePath();

        if(check.equals("ForYourSystem"))
        {
            textField.setText(path);
        }
        else
        {
            textField_6.setText(path);
        }
    }

    public static void getHash(byte[] inputBytes, String algorithm)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(inputBytes);
            digestdBytes = messageDigest.digest();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void keyGeneration(String d_tKey)
    {
        getHash(d_tKey.getBytes(),"SHA-256");
    }


    public static void AESEncrypt(Cipher cipher,Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException 
    {

        try 
        {

            fis = new FileInputStream(file);
            //fos = new FileOutputStream(new File("D:\\JAINISH\\SEM-6\\Mini Project\\Run\\EncryptedImage.jpg"));
            fos = new FileOutputStream(new File("C:\\Users\\HP\\OneDrive\\Documents\\1_Images\\EncryptedImage.jpg"));

            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cipt = new CipherInputStream(fis, cipher);

            int i;
            while ((i = cipt.read()) != -1) 
            {
                fos.write(i);
            }            
            JOptionPane.showMessageDialog(null, "Done");
            System.out.println("Encryption Done");

            cipt.close();
            fis.close();
            fos.close();
        } 
        
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }


    public static void AESDecrypt(Cipher cipher,Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException 
    {

        try 
        {
     
            fis1 = new FileInputStream(file);
            //fos1 = new FileOutputStream(new File("D:\\JAINISH\\SEM-6\\Mini Project\\Run\\DecryptedImage.jpg"));// creates output stream with same object so that we can change desired image
            fos1 = new FileOutputStream(new File("C:\\Users\\HP\\OneDrive\\Documents\\1_Images\\DecryptedImage.jpg"));

            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream ciptt1 = new CipherInputStream(fis1, cipher);

            int j;
            while ((j = ciptt1.read()) != -1) 
            {
                fos1.write(j);
            }
                    
            JOptionPane.showMessageDialog(null, "Done");
            System.out.println("Decryption Done");

            ciptt1.close();
            fis1.close();
            fos1.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
//--------------------diffie hallman starts here-----------------------------------

    static int power(int x, int y, int p)
    {
        int res = 1;  

        x = x % p; 

        while (y > 0)
        {
            if (y % 2 == 1)
            {
                res = (res * x) % p;
            }

            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }
        return res;
    }

    public static void findPrimefactors(HashSet<Integer> s, int n)
    {
        while (n % 2 == 0)
        {
            s.add(2);
            n = n / 2;
        }

        for (int i = 3; i <= Math.sqrt(n); i = i + 2)
        {
            while (n % i == 0)
            {
                s.add(i);
                n = n / i;
            }
        }
        if (n > 2)
        {
            s.add(n);
        }
    }

    public static int findPrimitive(int n)
    {
        HashSet<Integer> s = new HashSet<Integer>();
        int phi = n - 1;

        findPrimefactors(s, phi);

        for (int r = 2; r <= phi; r++)
        {
            boolean flag = false;
            for (Integer a : s)
            {
                if (power(r, phi / (a), n) == 1)
                {
                    flag = true;
                    break;
                }
            }
            if (flag == false)
            {
                return r;
            }
        }
        return -1;
    }

    public static void diffieHellman()
    {
        primeNumber = Integer.parseInt(textField_2.getText());
        privateInt = Integer.parseInt(textField_3.getText());
        primitiveRoot = findPrimitive(primeNumber);

        // System.out.println(" Smallest primitive root of " + primeNumber
        //         + " is " + findPrimitive(primeNumber));

        
        pubKey = (Math.pow(primitiveRoot,privateInt))%primeNumber;
        textField_4.setText(String.valueOf(pubKey));
    }

//----------------------------------------------------------------------------------------------
    
    //Initialize the contents of the first frame.
     
    private static void initializeFirst() 
    {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Mini Project");
        frame.getContentPane().setBackground(new Color(206,248,248));

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                if(path!=null)
                {
                    Path pathToBeDeleted = Paths.get(path);
                    try 
                    {
                        Files.delete(pathToBeDeleted);

                    } 
                    catch (IOException f) 
                    {
                        f.printStackTrace();
                    }
                }
                System.exit(0);

            }
        });


        frame.getContentPane().setLayout(null);
        
        JLabel lbl1 = new JLabel("Image Encryption And Decryption");
        lbl1.setHorizontalAlignment(SwingConstants.CENTER);
        lbl1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lbl1.setBounds(140, 30, 292, 28);
        frame.getContentPane().add(lbl1);
        
        JButton btnForYourSystem = new JButton("For Your System");
        btnForYourSystem.setBackground(new Color(206,213,219));
        btnForYourSystem.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnForYourSystem.setBounds(90, 165, 180, 40);
        btnForYourSystem.setFocusable(false);
        frame.getContentPane().add(btnForYourSystem);

        btnForYourSystem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
               initializeSecond();
            }
        });
        
        JButton btnToShareAnother = new JButton("To Share Another User");
        btnToShareAnother.setBackground(new Color(206,213,219));
        btnToShareAnother.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnToShareAnother.setBounds(320, 165, 180, 40);
        btnToShareAnother.setFocusable(false);
        frame.getContentPane().add(btnToShareAnother);

        btnToShareAnother.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
               initializeThird();
            }
        });
    }

    private static void initializeSecond() 
    {
        frame.getContentPane().removeAll();     //remove all previous component and add new component
        frame.repaint();

        JLabel lbl2 = new JLabel("For Your System");
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);
        lbl2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lbl2.setBounds(140, 30, 292, 28);
        frame.getContentPane().add(lbl2);
        
        JButton btnEncryption = new JButton("Encryption");
        btnEncryption.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnEncryption.setBackground(new Color(206,213,219));
        btnEncryption.setBounds(90, 220, 180, 40);
        btnEncryption.setFocusable(false);
        frame.getContentPane().add(btnEncryption);

        btnEncryption.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                String tKey = textField_1.getText();
                if(tKey!=null)
                {
                    keyGeneration(tKey);

                    SecretKeySpec secretKeySpec = new SecretKeySpec(digestdBytes, "AES");

                    try
                    {
                        Cipher cipher = Cipher.getInstance("AES");
                        AESEncrypt(cipher,secretKeySpec);
                    } 
                    catch(Exception ex) 
                    {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    textField_1.setText("Please Enter key");
                }
            }
        });
        
        JButton btnDecryption = new JButton("Decryption");
        btnDecryption.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnDecryption.setBackground(new Color(206, 213, 219));
        btnDecryption.setBounds(336, 220, 170, 40);
        btnDecryption.setFocusable(false);
        frame.getContentPane().add(btnDecryption);

        btnDecryption.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                String tKey = textField_1.getText();
                keyGeneration(tKey);

                SecretKeySpec secretKeySpec = new SecretKeySpec(digestdBytes, "AES");

                try
                {
                    Cipher cipher = Cipher.getInstance("AES");
                    AESDecrypt(cipher,secretKeySpec);
                } 
                catch(Exception ex) 
                {
                    ex.printStackTrace();
                }
            }
        });
        
        JLabel lblNewLabel = new JLabel("Select Image : ");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblNewLabel.setBounds(58, 99, 105, 22);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblAddKey = new JLabel("Enter Key : ");
        lblAddKey.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblAddKey.setBounds(58, 149, 105, 22);
        frame.getContentPane().add(lblAddKey);
        
        textField = new JTextField();
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField.setBounds(173, 101, 259, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        textField_1 = new JTextField();
        textField_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_1.setColumns(10);
        textField_1.setBounds(173, 149, 259, 20);
        frame.getContentPane().add(textField_1);
        
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setBackground(new Color(206,213,219));
        btnBrowse.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnBrowse.setBounds(460, 100, 89, 23);
        frame.getContentPane().add(btnBrowse); 

        btnBrowse.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                openDialog("ForYourSystem");
            }
        });
    
    }

    private static void initializeThird() 
    {
        frame.getContentPane().removeAll();     //remove all previous component and add new component
        frame.repaint();

        JLabel lbl3 = new JLabel("To Share Another User");
        lbl3.setHorizontalAlignment(SwingConstants.CENTER);
        lbl3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lbl3.setBounds(140, 30, 292, 28);
        frame.getContentPane().add(lbl3);
        
        JLabel lblPrime = new JLabel("Enter Prime Number : ");
        lblPrime.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblPrime.setBounds(40, 99, 171, 22);
        frame.getContentPane().add(lblPrime);
        
        JLabel lblP_Int = new JLabel("Enter Private Integer : ");
        lblP_Int.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblP_Int.setBounds(40, 139, 171, 22);
        frame.getContentPane().add(lblP_Int);
        
        JLabel lblP_Int_1 = new JLabel("*Less than Prime Number ");
        lblP_Int_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblP_Int_1.setBounds(439, 139, 147, 22);
        frame.getContentPane().add(lblP_Int_1);
        
        JLabel lblPublicKey = new JLabel("Public Key : ");
        lblPublicKey.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblPublicKey.setBounds(40, 179, 171, 22);
        frame.getContentPane().add(lblPublicKey);

        JButton btnGenerate = new JButton("Generate");
        btnGenerate.setBackground(new Color(206,213,219));
        btnGenerate.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnGenerate.setBounds(461, 179, 89, 23);
        frame.getContentPane().add(btnGenerate);
        
        btnGenerate.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                diffieHellman();
            }
        });
        
        JLabel lblRecPublicKey_1 = new JLabel("Enter Reciever's");
        lblRecPublicKey_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblRecPublicKey_1.setBounds(40, 215, 171, 22);
        frame.getContentPane().add(lblRecPublicKey_1);
        
        JLabel lblRecPublicKey_2 = new JLabel("Public Key : ");
        lblRecPublicKey_2.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblRecPublicKey_2.setBounds(50, 235, 94, 22);
        frame.getContentPane().add(lblRecPublicKey_2);
        
        JLabel lblMessage = new JLabel("*You have to share prime number and public key to another user.");
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblMessage.setBounds(76, 330, 448, 22);
        frame.getContentPane().add(lblMessage);
        
        textField_2 = new JTextField();
        textField_2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_2.setBounds(237, 100, 195, 20);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);
        
        textField_3 = new JTextField();
        textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_3.setColumns(10);
        textField_3.setBounds(237, 140, 195, 20);
        frame.getContentPane().add(textField_3);
        
        textField_4 = new JTextField();
        textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_4.setColumns(10);
        textField_4.setBounds(237, 180, 195, 20);
        frame.getContentPane().add(textField_4);
        
        textField_5 = new JTextField();
        textField_5.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_5.setColumns(10);
        textField_5.setBounds(237, 220, 195, 20);
        frame.getContentPane().add(textField_5);                
        
        JButton btnGen_PrivateKey = new JButton("Generate Private Key");
        btnGen_PrivateKey.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnGen_PrivateKey.setBackground(new Color(206,213,219));
        btnGen_PrivateKey.setBounds(200, 270, 200, 35);
        btnGen_PrivateKey.setFocusable(false);
        frame.getContentPane().add(btnGen_PrivateKey);
        
        btnGen_PrivateKey.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                otherUserPubKey = Integer.parseInt(textField_5.getText());
                privateKey = (Math.pow(otherUserPubKey,privateInt))%primeNumber;
                initializeFourth();
            }
        });
        
    }

    private static void initializeFourth() 
    {
        frame.getContentPane().removeAll();     //remove all previous component and add new component
        frame.repaint();

        JLabel lbl2 = new JLabel("To Share Another User");
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);
        lbl2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lbl2.setBounds(140, 30, 292, 28);
        frame.getContentPane().add(lbl2);
        
        JButton btnEncryption = new JButton("Encryption");
        btnEncryption.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnEncryption.setBackground(new Color(206,213,219));
        btnEncryption.setBounds(90, 220, 180, 40);
        btnEncryption.setFocusable(false);
        frame.getContentPane().add(btnEncryption);

         btnEncryption.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                String tKey = textField_7.getText();
                keyGeneration(tKey);

                SecretKeySpec secretKeySpec = new SecretKeySpec(digestdBytes, "AES");

                try
                {
                    Cipher cipher = Cipher.getInstance("AES");
                    AESEncrypt(cipher,secretKeySpec);
                } 
                catch(Exception ex) 
                {
                    ex.printStackTrace();
                }
            }
        });
            
        JButton btnDecryption = new JButton("Decryption");
        btnDecryption.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnDecryption.setBackground(new Color(206, 213, 219));
        btnDecryption.setBounds(336, 220, 170, 40);
        btnDecryption.setFocusable(false);
        frame.getContentPane().add(btnDecryption);

        btnDecryption.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                String tKey = textField_7.getText();
                keyGeneration(tKey);

                SecretKeySpec secretKeySpec = new SecretKeySpec(digestdBytes, "AES");

                try
                {
                    Cipher cipher = Cipher.getInstance("AES");
                    AESDecrypt(cipher,secretKeySpec);
                } 
                catch(Exception ex) 
                {
                    ex.printStackTrace();
                }
            }
        });
        
        JLabel lblNewLabel = new JLabel("Select Image : ");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblNewLabel.setBounds(58, 99, 105, 22);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblAddKey = new JLabel("Key : ");
        lblAddKey.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblAddKey.setBounds(58, 149, 105, 22);
        frame.getContentPane().add(lblAddKey);
        
        JLabel lblMessage = new JLabel("*If you are sender then click on Encryption and If you are reciever then click on Decryption.");
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setFont(new Font("Times New Roman", Font.BOLD, 13));
        lblMessage.setBounds(0, 324, 586, 28);
        frame.getContentPane().add(lblMessage);
        
        textField_6 = new JTextField();
        textField_6.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_6.setBounds(173, 101, 259, 20);
        frame.getContentPane().add(textField_6);
        textField_6.setColumns(10);
        
        textField_7 = new JTextField();
        textField_7.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        textField_7.setColumns(10);
        textField_7.setBounds(173, 149, 259, 20);
        frame.getContentPane().add(textField_7);
        
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setBackground(new Color(206,213,219));
        btnBrowse.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnBrowse.setBounds(460, 100, 89, 23);
        frame.getContentPane().add(btnBrowse);

        textField_7.setText(String.valueOf(privateKey));
        
        btnBrowse.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                openDialog("ToShareAnotherUser");
            }
        });

    }

    
    //Launch the application.
     
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run()
            {
                try 
                {
                    Design window = new Design();
                    window.frame.setVisible(true);
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }

}