package spittr.data;

import spittr.Spittle;

import java.util.List;

public interface SpittleRepository {
    List<Spittle> findSpittles(long max,int count);
    public void save(Spittle spittle);
    public Spittle findOne(long id);
    public List<Spittle> findRecentSpittles();
}
