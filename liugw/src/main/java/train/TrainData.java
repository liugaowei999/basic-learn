package train;

import java.math.BigDecimal;

public class TrainData {
    private Integer transport_code;
    private Integer start_city_id;
    private Integer start_station_id;
    private String start_time;
    private Integer start_sequence;
    private Integer end_city_id;
    private Integer end_station_id;
    private String end_time;
    private Integer end_sequence;
    private Integer run_time;
    private String traffic_type;
    private BigDecimal price;


    public Integer getTransport_code() {
        return transport_code;
    }

    public void setTransport_code(Integer transport_code) {
        this.transport_code = transport_code;
    }

    public Integer getStart_city_id() {
        return start_city_id;
    }

    public void setStart_city_id(Integer start_city_id) {
        this.start_city_id = start_city_id;
    }

    public Integer getStart_station_id() {
        return start_station_id;
    }

    public void setStart_station_id(Integer start_station_id) {
        this.start_station_id = start_station_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Integer getStart_sequence() {
        return start_sequence;
    }

    public void setStart_sequence(Integer start_sequence) {
        this.start_sequence = start_sequence;
    }

    public Integer getEnd_city_id() {
        return end_city_id;
    }

    public void setEnd_city_id(Integer end_city_id) {
        this.end_city_id = end_city_id;
    }

    public Integer getEnd_station_id() {
        return end_station_id;
    }

    public void setEnd_station_id(Integer end_station_id) {
        this.end_station_id = end_station_id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Integer getEnd_sequence() {
        return end_sequence;
    }

    public void setEnd_sequence(Integer end_sequence) {
        this.end_sequence = end_sequence;
    }

    public Integer getRun_time() {
        return run_time;
    }

    public void setRun_time(Integer run_time) {
        this.run_time = run_time;
    }

    public String getTraffic_type() {
        return traffic_type;
    }

    public void setTraffic_type(String traffic_type) {
        this.traffic_type = traffic_type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
