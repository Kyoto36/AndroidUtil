import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * @ClassName: RetrofitKotlinExtension
 * @Description:
 * @Author: ls
 * @Date: 2020/9/16 19:01
 */

val TEXT_MEDIA = "text/plain".toMediaTypeOrNull()
val JSON_MEDIA = "application/json;charset=utf-8".toMediaTypeOrNull()

fun Map<String,Any>.toRequestBody(): RequestBody {
    val json = JSONObject(this).toString()
    return json.toRequestBody(JSON_MEDIA)
}
