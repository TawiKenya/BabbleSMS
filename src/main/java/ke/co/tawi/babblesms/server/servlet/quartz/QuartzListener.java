/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import ke.co.tawi.babblesms.test.quartz.QuartzJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzListener extends HttpServlet implements ServletContextListener {
        /**
	 * 
	 */
	private static final long serialVersionUID = -594007090895227780L;
		Scheduler scheduler = null;
        
        

        @Override
        public void contextInitialized(ServletContextEvent servletContext) {

                /*
                try {
                        // Setup the Job class and the Job group
                        JobDetail job = newJob(QuartzJob.class).withIdentity(
                                        "CronQuartzJob", "Group").build();

                        // Create a Trigger that fires every 5 minutes.
                        Trigger trigger = newTrigger()
                        .withIdentity("TriggerName", "Group")
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule("20 20 23 ? * *"))
                        .build(); 

                        // Setup the Job and Trigger with Scheduler & schedule jobs
                        scheduler = new StdSchedulerFactory().getScheduler();
                        scheduler.start();
                        scheduler.scheduleJob(job, trigger);
                }
                catch (SchedulerException e) {
                        e.printStackTrace();
                }*/
        }

        @Override
        public void contextDestroyed(ServletContextEvent servletContext) {
             //   System.out.println("Context Destroyed");
                try 
                {
                        scheduler.shutdown();
                } 
                catch (SchedulerException e) 
                {
                        e.printStackTrace();
                }
        }
}