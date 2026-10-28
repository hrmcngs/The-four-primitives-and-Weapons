import the_four_primitives_and_weapons.skill.HeldChargeTiming;
public class HeldChargeTimingTest {
 public static void main(String[] args) {
  long start=100;
  for(int callback=0;callback<1000;callback++) {
   if(HeldChargeTiming.elapsed(start,119)>=20)throw new AssertionError("Repeated callbacks cannot shorten charge");
  }
  check(HeldChargeTiming.elapsed(start,120)==20,"Minimum charge requires 20 world ticks");
  check(HeldChargeTiming.elapsed(start,159)<60,"Full charge cannot complete early");
  check(HeldChargeTiming.elapsed(start,160)==60,"Full charge requires 60 world ticks");
  check(HeldChargeTiming.elapsed(-1,160)==0,"Canceled charge cannot retain time");
  check(HeldChargeTiming.elapsed(200,100)==0,"World-time rollback cannot complete charge");
  check(HeldChargeTiming.elapsed(0,Long.MAX_VALUE)==Integer.MAX_VALUE,"Long holds do not overflow");
  check(HeldChargeTiming.elapsed(160,160)==0,"Next gesture starts empty");
  check(HeldChargeTiming.releasedPercent(100,119,20,60)<0,"Server rejects release before one second");
  check(HeldChargeTiming.releasedPercent(100,120,20,60)==20f/60,"Server computes minimum charge from its clock");
  check(HeldChargeTiming.releasedPercent(100,160,20,60)==1,"Server computes full charge after three seconds");
  check(HeldChargeTiming.releasedPercent(-1,160,20,60)<0,"Release without a start is rejected");
  check(HeldChargeTiming.releasedPercent(100,1000,20,60)==1,"Long holds clamp to full charge");
  System.out.println("Held charge elapsed-time and repeated-callback regression checks passed");
 }
 static void check(boolean valid,String message){if(!valid)throw new AssertionError(message);}
}
