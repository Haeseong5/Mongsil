import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView.OnScrollListener를 상속받는 PaginationScrollListener 유틸 클래스를 만들어 줌으로써,
 * RecyclerView 의 최상단, 최하단 스크롤 이벤트를 감지한다.
 */

//LinearLayout Support only
abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount //리사이클러뷰에 붙어있는 아이템 수
        val totalItemCount = layoutManager.itemCount //어댑터에 바인드된 아이템 수
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() //Returns the adapter position of the first visible view.
        /**
         * 리스트의 끝이 아니라 마지막 아이템이 보이기 시작하면서 부터 이 이벤트는 동작을 하게 된다.
         */
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                loadMoreItems()
            }
        }
    }

    companion object {
        //스크롤 사이즈 설정
        private const val PAGE_SIZE = 10
    }

}