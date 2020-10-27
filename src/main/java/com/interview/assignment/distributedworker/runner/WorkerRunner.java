package com.interview.assignment.distributedworker.runner;

import com.interview.assignment.distributedworker.model.WorkerModel;
import com.interview.assignment.distributedworker.service.HttpService;
import com.interview.assignment.distributedworker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Profile("!test")
@Component
public class WorkerRunner implements CommandLineRunner {

    @Autowired
    WorkerService workerService;

    @Autowired
    HttpService httpService;

    @Autowired
    ApplicationContext context;

    Logger logger = LogManager.getLogger(WorkerRunner.class);


    @Override
    public void run(String... args) throws Exception {
        boolean repeat = true;
        do {
           repeat =  runLoop();

        } while(repeat);

        logger.info("No more jobs. Goodbye");
        shutDown(0);
    }

    private void shutDown(int returnCode){
        SpringApplication.exit(context,()->returnCode);
    }

    private boolean runLoop(){
        int jobId = -1;
        try {
            jobId = workerService.getJobID();
            logger.info(jobId == -1 ? "" : "JobId : " + jobId);

        } catch (Exception e){
            logger.info("Something went wrong running the stored function, check your database.");
            return false;
        }

        if(jobId == -1){
            //IF I didn't find any jobs I exit here.
            return false;
        }

        WorkerModel model = null;
        long id = jobId;

        try {
             model = workerService.findById(id);
             String url = model.getUrl();
             Map<String, String> response = httpService.fetch(url);
             String http_code = response.get("http_code");

             if (http_code.equals("ERROR")) {
                 //If httpService threw and exception...
                 model.setStatus("ERROR");
                 model.setHttp_code(null);

             } else {
                 model.setStatus("DONE");
                 model.setHttp_code(http_code);
             }

        } catch (Exception e){
            //if there was an exception inside this whole thing with something else  I handle it here
            model.setStatus("ERROR");
            model.setHttp_code(null);

        } finally {
            workerService.save(model);
        }

        return true;
    }
}
