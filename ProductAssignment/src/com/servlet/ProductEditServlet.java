package com.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProductEditServlet
 * This was created by Nhan Tam Huynh ST# 991431626 on 10/15/2017 for Assignment 2
 * This is a servlet that handles HTML to update, add, and view the Product DataBase in sql 
 */
@WebServlet("/ProductEditServlet")
public class ProductEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//global variables   
	Connection con;
	PreparedStatement pst;  
	ResultSet rs;
	Statement st;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductEditServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//getting button parameters
		String addBtn = request.getParameter("addProduct");
		String viewBtn = request.getParameter("viewProduct");		
		String editBtn = request.getParameter("editAmount");

		//connection info
		String connectionUrl = "jdbc:mysql://localhost:3306/Product";
		String user = "root";
		String password = "Hgotfsd1";

		String productId = request.getParameter("productId");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		//checks to see which button was chosen
		if(addBtn != null) {

			//getting all form information
			String productName = request.getParameter("productName");
			String price = request.getParameter("price");
			double priceD = Double.parseDouble(price);
			String quantity = request.getParameter("quantity");
			int quantityI = Integer.parseInt(quantity);
			String category = request.getParameter("category");

			// sql database query
			String addProduct = "Insert into PRODUCT (PRODUCTID, PRODUCTNAME, QUANTITY, PRICE, CATEGORY) Values (?,?,?,?,?)";

			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(connectionUrl, user, password);
				pst = con.prepareStatement(addProduct);
				pst.setString(1, productId);
				pst.setString(2, productName);
				pst.setInt(3, quantityI);
				pst.setDouble(4, priceD);
				pst.setString(5, category);
				pst.executeUpdate();

			} catch(SQLException e){
				e.printStackTrace();
			}catch(Exception t) {
				t.printStackTrace();
			}finally {
				RequestDispatcher rd = request.getRequestDispatcher("AddSuccess.html");
				rd.forward(request, response);

			}
		}else if (viewBtn != null) {
			String searchQuery = "SELECT * FROM Product WHERE PRODUCTID = ?";
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(connectionUrl, user, password);
				pst = con.prepareStatement(searchQuery);
				pst.setString(1, productId);
				rs = pst.executeQuery();

				out.println("<!doctype html><html>");
				out.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"stylesCSS.css\"><head>");
				out.println("<body><h2>ProductID# "+ productId +" Information</h2>");
				out.println("<div id='holder'><table border=1>");
				out.println("<tr><td>ProductID</td><td>Name</td><td>Quantity</td><td>Price</td><td>Category</td></tr>");

				while(rs.next()) {
					String id = rs.getString(1);
					String name = rs.getString(2);
					int prdQuantitiy = rs.getInt(3);
					double prdPrice = rs.getDouble(4);
					String prdcategory = rs.getString(5);
					// makes table
					out.println("<tr><td>"+id+"</td><td>"+name+"</td><td>"+prdQuantitiy+"</td><td>"+prdPrice+"</td><td>"+prdcategory+"</td>");
				}//adds instructions as marquee
				out.println("</tr></table></div><marquee style=\"font-size:20px; background-color:#F0E68C\">Press Back to do More!</marquee></body></html>");

			}catch(SQLException e){
				e.printStackTrace();
			}catch(Exception t) {
				t.printStackTrace();
			}
		}else if(editBtn != null) {
			String price = request.getParameter("price");
			double priceD = Double.parseDouble(price);
			String quantity = request.getParameter("quantity");
			int quantityI = Integer.parseInt(quantity);
			
			if(price != null && productId != null && quantity.equals("")) {
				String editAmount = "Update Product set PRICE = ? WHERE PRODUCTID = ?";

				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					con = DriverManager.getConnection(connectionUrl, user, password);
					pst = con.prepareStatement(editAmount);
					pst.setDouble(1, priceD);
					pst.setString(2, productId);
					pst.executeUpdate();

				}catch(SQLException e){
					e.printStackTrace();
				}catch(Exception t) {
					t.printStackTrace();
				}finally {
					RequestDispatcher rd = request.getRequestDispatcher("EditSuccess.html");
					rd.forward(request, response);	
				}	
			}else {
				String editAmount = "Update Product set QUANTITY = ?, PRICE = ? WHERE PRODUCTID = ?";

				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					con = DriverManager.getConnection(connectionUrl, user, password);
					pst = con.prepareStatement(editAmount);
					pst.setInt(1, quantityI);
					pst.setDouble(2, priceD);
					pst.setString(3, productId);
					pst.executeUpdate();

				}catch(SQLException e){
					e.printStackTrace();
				}catch(Exception t) {
					t.printStackTrace();
				}finally {
					RequestDispatcher rd = request.getRequestDispatcher("AddSuccess.html");
					rd.forward(request, response);	
			}
			}

		}
	}

}
