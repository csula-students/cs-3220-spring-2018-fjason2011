package edu.csula.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.csula.storage.servlet.EventsDAOImpl;
import edu.csula.storage.EventsDAO;
import edu.csula.models.Event;

@WebServlet("/admin/events")
public class AdminEventsServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// TODO: render the events page HTML
		EventsDAO dao = new EventsDAOImpl(getServletContext());
		Collection<Event> events = dao.getAll();
		System.out.println(events);

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>Incremental Game</title>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../app.css \">");
		out.println("</head>");

		out.println("<body>");
		out.println("<h1>Incremental Game</h1>");
		out.println("<nav>");
		out.println("<a href=\"admin-info.html\">Game Information</a>");
		out.println(" | ");
		out.println("<a href=\"admin-gen.html\">Generators</a>");
		out.println(" | ");
		out.println("<a href=\"admin-events.html\">Events</a>");
		out.println("</nav>");

		out.println("<form method=\"POST\">");
		out.println("<label for=\"eventname\">Event Name</label><br>");
		out.println("<input type=\"text\" name=\"name\" id=\"eventname\"><br>");
		out.println("<label for=\"eventDescription\">Event Description</label><br>");
		out.println("<textarea name=\"description\" id=\"eventDescription\"></textarea><br>");
		out.println("<label for=\"triggerAt\">Trigger at</label><br>");
		out.println("<input type=\"number\" name=\"trigger\" id=\"triggerAt\"><br>");
		out.println("<button>Add{Edit}</button>");
		out.println("</form>");

		out.println("<table>");
		out.println("<tr>");
		out.println("<th>Name</th>");
		out.println("<th>Description</th>");
		out.println("<th>TriggerAt</th>");
		out.println("<th>Action</th>");
		out.println("</tr>");
		for (Event event : events) {
			out.println("<tr>");
			out.println("<td>" + event.getName() + "</td>");
			out.println("<td>" + event.getDescription() + "</td>");
			out.println("<td>" + event.getTriggerAt() + "</td>");
			out.println("<td>");
			out.println("<a href=\"EditServlet?id=" + event.getId() +"\">Edit</a>");
			out.println("|");
			out.println("<a href=\"DeleteServlet?id=" + event.getId() + "\">delete</a>");
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table>");

		out.println("</body>");
		out.println("</html>");
	}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: handle upsert transaction
		
		String name = request.getParameter("event_name");
		String description = request.getParameter("event_description");
		int triggerAt = Integer.parseInt(request.getParameter("trigger"));

		EventsDAO dao = new EventsDAOImpl(getServletContext());
		Collection<Event> events = dao.getAll();
		

		dao.add(new Event(events.size(), name, description, triggerAt));
		response.sendRedirect("/admin/events");
	}
}
