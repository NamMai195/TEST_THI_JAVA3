package com.poly.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import poly.dao.USERSDao;
import poly.entity.USERS;

@WebServlet({ "/USERS/QLNguoiDung",
              "/USERS/create", 
              "/USERS/update", 
              "/USERS/delete", 
              "/USERS/edit", "/USERS/reset"
              
             })
public class USERSServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        USERS form = new USERS();
        USERSDao dao = new USERSDao();
        String role = req.getParameter("role");
        System.out.println("Role received: " + role);  // Log the role value

        try {
            // Register the DateConverter if needed (use the correct date pattern)
            DateConverter dateConverter = new DateConverter(null);
            dateConverter.setPattern("yyyy-MM-dd"); // Adjust the pattern as per your need
            ConvertUtils.register(dateConverter, Date.class);

            // Populate the form with the request parameters
            BeanUtils.populate(form, req.getParameterMap());

            // Handle the role
            if (role != null) {
                form.setAdmin(Boolean.parseBoolean(role)); // Set the role as true or false
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to populate form data.");
            return;
        }

        String path = req.getServletPath();
        
        // Handle search action first
        String action = req.getParameter("action");
        if ("search".equals(action)) {
            String searchId = req.getParameter("searchId");
            try {
                form = dao.selectByName(searchId); // Search by ID or name
                if (form == null) {
                    req.setAttribute("error", "User not found.");
                } else {
                    req.setAttribute("user", form); // Pass the user to the JSP
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "An error occurred during the search: " + e.getMessage());
            }
        } else {
            try {
                if (path.equals("/USERS/edit")) {
                    String id = req.getPathInfo() != null ? req.getPathInfo().substring(1) : null;
                    if (id != null) {
                        form = dao.selectByid(id);
                        if (form == null) {
                            req.setAttribute("error", "User not found.");
                        }
                    } else {
                        req.setAttribute("error", "Invalid user ID.");
                    }
                } else if (path.equals("/USERS/create")) {
                    dao.insert(form);
                    req.setAttribute("message", "User added successfully.");
                } else if (path.equals("/USERS/update")) {
                    dao.update(form);
                    req.setAttribute("message", "User updated successfully.");
                } else if (path.equals("/USERS/delete")) {
                    String id = req.getParameter("id");
                    if (id != null) {
                        dao.delete(id);
                        req.setAttribute("message", "User deleted successfully.");
                    } else {
                        req.setAttribute("error", "Invalid user ID.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "An error occurred during the operation: " + e.getMessage());
            }
        }

        // Reset form after create, delete, or update
        if (path.equals("/USERS/create") || path.equals("/USERS/delete") || path.equals("/USERS/update") || path.equals("/USERS/reset") ) {
            form = new USERS(); // Reset the form
        }

        List<USERS> list = dao.selectAll(); // Fetch all users
        req.setAttribute("list", list); // Pass the list of users to the JSP
        req.getRequestDispatcher("/index.jsp").forward(req, resp); // Forward to the JSP page
    }

}
