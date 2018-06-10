
package ServerNotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Will
 */
public class ClientServlet extends HttpServlet {

    public StringBuffer str = new StringBuffer("testing");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClientServlet</title>");
            out.println("</head>");
            out.println("<body>");

            // Html version of all my print line code
            /*
            <form>
            <input type="text" name="host" />Host"
            <br />
            <input type="text" name="port" />Port
            <br />
            <input type="text" name="request" />Request
            <br /><br />
            <textarea>

            This is where I run my method that connects, sends my request
            and then displays the response
            
            </textarea>
            <br />
            <input type="submit" name="connect" value="Send Request" />

            </form>
             */
            out.println("<form action=\"\" method=\"post\">");
            out.println("<input type=\"text\" name=\"host\" value=\"" + request.getParameter("host") + "\"/>Host");
            out.println("<br />");
            out.println("<input type=\"text\" name=\"port\"value=\"" + request.getParameter("port") + "\"/>Port");
            out.println("<br />");
            out.println("<input type=\"text\" name=\"request\" />Request");
            out.println("<br />");
            out.println("<br />");
            out.println("<textarea>");

            // You can have multiple submit buttons with differnt names
            // to do different things. Here by button is named connect.
            // This action takes place only after the button is clicked
            if (request.getParameter("connect") != null) {
                

                // Creates a new execturService to run our thread from
                ExecutorService executor = Executors.newSingleThreadExecutor();
                
                // Callable is a thread that returns a value
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() {
                        // Calls the makeConnection() and returns response through the callable
                        return makeConnection(request);
                    }
                };
                
                // Stores the value returned from our method. 
                Future<String> future = executor.submit(callable);
                
                // Closes the execurtorService after its done
                executor.shutdown();
                
                try {
                    // Displays our value from makeConnection by accessing future.get()
                    out.println(future.get());

                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            out.println("</textarea>");

            out.println("<br />");
            out.println("<input type=\"submit\" name=\"connect\" value=\"Send Request\" />");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // I pass HttpServerletRequest and HttpServletResponse params so they can
    // interact with the code from the servlet

    private String makeConnection(HttpServletRequest request) {

        // This is bascially the same code from class that you can find in
        // "Server Client Example.docx"
        String host = request.getParameter("host");
        String portStr = request.getParameter("port");
        String from = "";

        Socket client = null;
        // Original variables named out and in, i renamed to avoid conflicts with
        // the parameters. Changed following code accordingly
        PrintWriter toServerOut = null;
        BufferedReader fromServerIn = null;

        new Thread() {
            
        };
        
        try {
            // Connects to the running server based on info in form
            client = new Socket(host, Integer.parseInt(portStr));

            // In / Output streams to communicate to / from server
            toServerOut = new PrintWriter(client.getOutputStream(), true);
            fromServerIn = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            // This gets the text in the text input "request"
            String toServer = request.getParameter("request");
            //send request to server
            toServerOut.println(toServer);
            //read response from server
            String fromServer = fromServerIn.readLine();
            //display
            // It will display where the method is called. That is why I placed
            // the click action in the text area, so it displays there

            from = fromServer;
        } catch (IOException ex) {

        } finally {
            try {
                if (fromServerIn != null) {
                    fromServerIn.close();
                }
                if (toServerOut != null) {
                    toServerOut.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException ex) {

            }
        }
        return from;
    }
    
} // End makeConnection

