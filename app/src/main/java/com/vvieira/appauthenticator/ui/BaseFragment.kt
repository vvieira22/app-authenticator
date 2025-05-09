import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import com.vvieira.appauthenticator.R

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    protected val toolbar: Toolbar?
        get() = (activity as? AppCompatActivity)?.findViewById(R.id.toolbar)

    protected val bottomNavigationView: BottomNavigationView?
        get() = activity?.findViewById(R.id.bottom_navigation)

    protected fun setToolbarTitle(title: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }

    protected fun setBottomNavigationViewVisibility(visibility: Int) {
        bottomNavigationView?.visibility = visibility
    }
}