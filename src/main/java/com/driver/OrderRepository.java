package com.driver;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRepository {

    private HashMap<String,Order>Odb=new HashMap<>();
    private HashMap<String,DeliveryPartner>Pdb=new HashMap<>();
    private HashMap<String,List<String>>OPdb=new HashMap<>();


    public void addOrder(Order order)
    {
        if(!Odb.containsKey(order.getId()))
        {
            Odb.put(order.getId(),order);
        }
        return;
    }
    public void addPartner(String partnerId)
    {
        if(!Pdb.containsKey(partnerId))
        {
            DeliveryPartner dp=new DeliveryPartner(partnerId);
            Pdb.put(partnerId,dp);
            OPdb.put(partnerId,new ArrayList<>());
        }
        return;
    }
    public void addOrderPartnerPair(String orderId,String partnerId)
    {
        if(Odb.containsKey(orderId)&&Pdb.containsKey(partnerId))
        {
            if(OPdb.containsKey(partnerId))
            {
                if (OPdb.get(partnerId).contains(orderId)) {
                    return;
                }
                OPdb.get(partnerId).add(orderId);
            }
        }
        return;

    }
    public Order getOrderById(String orderId)
    {
        if(Odb.containsKey(orderId))
        {
            return Odb.get(orderId);
        }
        return null;
    }
    public DeliveryPartner getPartnerById(String partnerId)
    {
        if(Pdb.containsKey(partnerId))
        {
            return Pdb.get(partnerId);
        }
        return null;
    }
    public Integer getOrderCountByPartnerId(String partnerId)
    {
        if(OPdb.containsKey(partnerId))
        {
            return OPdb.get(partnerId).size();
        }
        return -1;
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        if(OPdb.containsKey(partnerId))
        {
            return OPdb.get(partnerId);
        }
        return null;
    }
    public List<String> getAllOrders()
    {
        List<String>all=new ArrayList<>();
        for(String orderId:Odb.keySet())
        {
            all.add(orderId);
        }
        return all;
    }
    public Integer getCountOfUnassignedOrders()
    {
        int c=0;
        for(String orderId:Odb.keySet())
        {
            int flag=0;
            for(String PartnerId: OPdb.keySet())
            {
                if(OPdb.get(PartnerId).contains(orderId))
                {
                    flag=1;
                   break;
                }
            }
            if(flag==0)
            {
                c++;
            }
        }
        return c;

    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        Integer hour = Integer.valueOf(time.substring(0, 2));
        Integer minutes = Integer.valueOf(time.substring(3));
        Integer timeS = hour*60 + minutes;
        int countOfOrder=0;
        if(OPdb.containsKey(partnerId))
        {
            List<String>list=OPdb.get(partnerId);
            for(String order:list)
            {
                if(Odb.containsKey(order))
                {
                    Order currOrder=Odb.get(order);
                    if(timeS<currOrder.getDeliveryTime())
                    {
                        countOfOrder+=1;
                    }
                }
            }
        }
        return countOfOrder;

    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        Integer time = 0;
        List<String>list=OPdb.get(partnerId);
        for(String order:list)
        {
            if(Odb.containsKey(order))
            {
                Order currOrder=Odb.get(order);
                time=Math.max(time,currOrder.getDeliveryTime());
            }
        }
        Integer hour = time/60;
        Integer minutes = time%60;

        String hourInString = String.valueOf(hour);
        String minInString = String.valueOf(minutes);
        if(hourInString.length() == 1){
            hourInString = "0" + hourInString;
        }
        if(minInString.length() == 1){
            minInString = "0" + minInString;
        }

        return  hourInString + ":" + minInString;

    }
    public void deletePartnerById(String partnerId)
    {
        if(OPdb.containsKey(partnerId))
        {
            OPdb.remove(partnerId);
        }
        if(Pdb.containsKey(partnerId))
        {
            Pdb.remove(partnerId);
        }

    }
    public void deleteOrderById(String orderId)
    {
        if(Odb.containsKey(orderId))
        {
            Odb.remove(orderId);
        }
        for(String pid:OPdb.keySet())
        {
            if(OPdb.get(pid).contains(orderId))
            {
                OPdb.get(pid).remove(orderId);
            }
//            for(String oid: OPdb.get(pid))
//            {
//                if(oid.equals(orderId))
//                {
//                    OPdb.get(pid).remove(oid);
//                }
//            }
        }

    }

}
