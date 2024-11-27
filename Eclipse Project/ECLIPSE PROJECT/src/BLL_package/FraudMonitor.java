package BLL_package;

import java.util.LinkedList;
import java.util.Queue;

import admin_package.Admin;

public class FraudMonitor // singleton 
{
    private Queue<Anamoly> anamolies = new LinkedList<>();
    private boolean isActive;

    public FraudMonitor() 
    {
        this.isActive = false;
    }

    public void startMonitoring() 
    {
        this.isActive = true;
        System.out.println("Fraud Monitoring started.");
    }

    public void addAnomaly(Anamoly anomaly) 
    {
        if (isActive) 
        {
            anamolies.offer(anomaly);
            System.out.println("Anomaly added to the queue.");
        } 
        else 
        {
            System.out.println("Fraud monitoring is not active.");
        }
    }

    public void resolveAnomaly(Admin admin) 
    {
        if (!anamolies.isEmpty()) 
        {
            Anamoly anomaly = anamolies.poll();
            admin.resolveAnamoly(anomaly);
        } 
        else 
        {
            System.out.println("No anomalies to resolve.");
        }
    }

    public void stopMonitoring() 
    {
        this.isActive = false;
        System.out.println("Fraud Monitoring stopped.");
    }

	public Queue<Anamoly> getAnamolies() {
		return anamolies;
	}

	public void setAnamolies(Queue<Anamoly> anamolies) {
		this.anamolies = anamolies;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
