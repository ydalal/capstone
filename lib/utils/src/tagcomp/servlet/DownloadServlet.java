package tagcomp.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet
{
	private static final long serialVersionUID = -451904601098037390L;
	private ServletOutputStream ostream;
	private BufferedInputStream buf;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String fileName = req.getParameter("filename");

		try
		{
			if(fileName != null && fileName.length() > 0)
			{
				File file = new File(fileName);
				if(!file.isFile())
				{
					throw new Exception("File " + fileName + "does not exist");
				}
				
				buf = new BufferedInputStream(new FileInputStream(file));

				resp.setContentLength((int)file.length());
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("application/download");
				resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				ostream = resp.getOutputStream();
				int bytes = 0;
				while((bytes = buf.read()) != -1)
				{
					ostream.write(bytes);
				}
				ostream.flush();
				ostream.close();
			}
			else
			{
				throw new Exception("Invalid filename");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		finally
		{
			if(ostream != null)
			{
				ostream.close();
			}			
			if(buf != null)
			{
				buf.close();
			}
		}
	}
}