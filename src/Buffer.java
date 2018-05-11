
public class Buffer {

    private int id;
    private String msg;
    private Integer[] clockVet;

    public Buffer(int id, String msg, Integer[] clockVet) {
        this.id = id;
        this.msg = msg;
        this.clockVet = clockVet;
    }

    @Override
    public String toString() {
        return "Buffer{" + "id=" + id + ", msg=" + msg + ", clockVet=" + clockVet + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer[] getClockVet() {
        return clockVet;
    }

    public void setClockVet(Integer[] clockVet) {
        this.clockVet = clockVet;
    }

}
