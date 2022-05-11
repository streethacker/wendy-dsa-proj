package multithread.weather.assistant;

import multithread.weather.helper.RealtimeWeatherBo;
import multithread.weather.helper.ThirdPartyWeatherServiceHelper;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class RealtimeWeatherAssistant implements Closeable {

  private static final int defaultPoolSize = 5;
  private static final int defaultConnectTimeout = 3;
  private static final int defaultReadTimeout = 3;

  private ExecutorService executor;
  private ThirdPartyWeatherServiceHelper weatherServiceHelper;

  public RealtimeWeatherAssistant() {
    this(defaultPoolSize, defaultConnectTimeout, defaultReadTimeout);
  }

  public RealtimeWeatherAssistant(int poolSize, int connectTimeout, int readTimeout) {
    weatherServiceHelper = new ThirdPartyWeatherServiceHelper(connectTimeout, readTimeout);
    executor = Executors.newFixedThreadPool(poolSize);
  }

  public List<RealtimeWeatherBo> doQuery(List<String> cityList)
      throws ExecutionException, InterruptedException {
    List<Future<RealtimeWeatherBo>> futureList = new ArrayList<>();
    for (String cityName : cityList) {
      @SuppressWarnings("Convert2Lambda")
      Future<RealtimeWeatherBo> future =
          executor.submit(
              new Callable<RealtimeWeatherBo>() {
                @Override
                public RealtimeWeatherBo call() {
                  try {
                    return weatherServiceHelper.getRealtimeWeatherByCityName(cityName);
                  } catch (IOException e) {
                    System.out.println(
                        "IOException occurred inside: "
                            + Thread.currentThread().getName()
                            + ", error: "
                            + e.getMessage());
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  return null;
                }
              });
      futureList.add(future);
    }
    List<RealtimeWeatherBo> result = new ArrayList<>();
    for (Future<RealtimeWeatherBo> future : futureList) {
      RealtimeWeatherBo weatherBo = future.get();
      if (Objects.nonNull(weatherBo)) {
        result.add(weatherBo);
      }
    }
    return result;
  }

  @Override
  public void close() {
    System.out.println("Shutdown thread pool...");
    executor.shutdown();
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    List<String> cityList = new ArrayList<>();
    cityList.add("驻马店");
    cityList.add("上海");
    cityList.add("北京");
    cityList.add("镇江");
    cityList.add("杭州");
    try (RealtimeWeatherAssistant assistant = new RealtimeWeatherAssistant()) {
      List<RealtimeWeatherBo> result = assistant.doQuery(cityList);
      for (RealtimeWeatherBo weatherBo : result) {
        System.out.println(weatherBo);
      }
    }
  }
}
