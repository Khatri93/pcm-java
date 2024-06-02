package com.example.pcm;

import com.example.pcm.ui.BookManagementUI;
import com.example.pcm.ui.LoginRegisterForm;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        SwingUtilities.invokeLater(() -> {
            LoginRegisterForm form = new LoginRegisterForm();
            form.setVisible(true);
        });
//        new BookManagementUI();
    }
}
