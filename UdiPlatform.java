package udiplatform;
import java.sql.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


@Path("udiplatform")
public class UdiPlatform {

    @Context
    private UriInfo context;

    public UdiPlatform() {
    }
    
    public static void main(String[] args){    
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public static String getHtml(@QueryParam("newData") String newData,@QueryParam("parent") boolean parent,@QueryParam("update") String update, @QueryParam("action") String action, @QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName, @QueryParam("city") String city, @QueryParam("street") String street, @QueryParam("state") String state, @QueryParam("zip") String zip ) throws FileNotFoundException, IOException, ClassNotFoundException {

        String url="jdbc:mysql://localhost:3306/db1?useSSL=true";
        String user="root";
        String pass="";
        String msg="";
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn=DriverManager.getConnection(url,user,pass);
                System.out.println("connection successful");
                PreparedStatement ps;
                if(action.equals("create")){
                    if(parent==true){
                    ps = conn.prepareStatement("insert into userInfo (firstName,lastName,street,city,state,zip,parent)"+"values(?,?,?,?,?,?,?)");
                    ps.setString(1,firstName);
                    ps.setString(2,lastName);
                    ps.setString(3,street);
                    ps.setString(4,city);
                    ps.setString(5,state);
                    ps.setString(6,zip);
                    ps.setBoolean(7,parent);
                    ps.executeUpdate();
                    }
                    else{
                        ps = conn.prepareStatement("update userInfo set child_firstName=? where lastName=?");
                        ps.setString(1,firstName);
                        ps.setString(2,lastName);
                        ps = conn.prepareStatement("update userInfo set child_lastName=? where lastName=?");
                        ps.setString(1,lastName);
                        ps.setString(2,lastName);
                        ps.executeUpdate();
                        conn.close();                       
                    }
                    msg="<html>"+"<body>"+firstName+" "+lastName+", your information has been saved."+"</body>"+"</html>";
                    conn.close();
                }
                else if(action.equals("update")){
                    ps=conn.prepareStatement("update userInfo set "+update+"=? where lastName=?");
                    ps.setString(1,newData);
                    ps.setString(2,lastName);
                    ps.executeUpdate();
                    msg="<html>"+"<body>"+firstName+" "+lastName+", your new information of "+update+" has been updated."+"</body>"+"</html>";
                    conn.close();
                        
                }
                else if(action.equals("delete")){
                    ps=conn.prepareStatement("delete from userInfo where lastName=?");
                    ps.setString(1,lastName);
                    ps.executeUpdate();
                    msg="<html>"+"<body>"+firstName+" "+lastName+", your information has been deleted."+"</body>"+"</html>";
                    conn.close();
                }
                }
            catch(Exception e){
            System.out.println(e);
            }
        return msg;
    }

    
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
