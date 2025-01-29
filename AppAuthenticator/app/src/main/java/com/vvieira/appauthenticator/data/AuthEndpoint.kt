import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.util.CADASTRO
import com.vvieira.appauthenticator.util.LOGIN
import okhttp3.ResponseBody
import retrofit2.http.Path

interface AuthEndPoint {
    // VER COMO PARAMETRIZAR ESSE CARA DENTRO DO POST() URGENTE!!!!!!!!
    @POST(CADASTRO)
    fun register(@Path("type") type: String, @Body usr: RegisterModelRequest): Call<ResponseBody>

    @POST(LOGIN)
    fun login(@Path("type") type: String, @Body usr: LoginModelRequest): Call<ResponseBody>
}
