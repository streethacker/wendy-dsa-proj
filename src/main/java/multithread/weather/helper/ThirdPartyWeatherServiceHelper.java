package multithread.weather.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ThirdPartyWeatherServiceHelper {

  private static final ObjectMapper mapper = new ObjectMapper();

  /** 第三方实时天气查询API */
  private static final String thirdPartyUrl = "https://yiketianqi.com/free/day";

  /** 第三方授权appId */
  private static final String appId = "76852245";

  /** 第三方授权appSecret */
  private static final String appSecret = "v686Wew2";

  /** 默认链接超时时间，秒 */
  private static final Integer defaultConnectTimeout = 5;

  /** 默认请求超时时间，秒 */
  private static final Integer defaultReadTimeout = 3;

  /** okhttp3 客户端 */
  private OkHttpClient okHttpClient;

  public ThirdPartyWeatherServiceHelper() {
    this(defaultConnectTimeout, defaultReadTimeout);
  }

  public ThirdPartyWeatherServiceHelper(Integer connectTimeout, Integer readTimeout) {
    okHttpClient =
        new OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .build();
  }

  /**
   * 查询指定城市的实时天气信息
   *
   * @param cityName 查询的城市名称
   * @return RealtimeWeatherBo
   * @throws IOException http请求失败或Json转换失败时抛出
   */
  public RealtimeWeatherBo getRealtimeWeatherByCityName(String cityName) throws IOException {
    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(thirdPartyUrl)).newBuilder();
    urlBuilder.addQueryParameter("appid", appId);
    urlBuilder.addQueryParameter("appsecret", appSecret);
    urlBuilder.addQueryParameter("city", cityName);
    Request request = new Request.Builder().url(urlBuilder.build().toString()).build();
    Call call = okHttpClient.newCall(request);
    try (Response response = call.execute()) {
      if (response.isSuccessful() && Objects.nonNull(response.body())) {
        //noinspection ConstantConditions
        return mapper.readValue(response.body().string(), RealtimeWeatherBo.class);
      }
      throw new IOException(
          "Request third party weather service failed: code="
              + response.code()
              + ", message="
              + response.message());
    }
  }

  public static void main(String[] args) {
    ThirdPartyWeatherServiceHelper helper = new ThirdPartyWeatherServiceHelper();
    try {
      System.out.println(helper.getRealtimeWeatherByCityName("上海"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
