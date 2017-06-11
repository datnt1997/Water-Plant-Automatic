package kris.hazen.dev.fulliot.Fragments;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kris.hazen.dev.fulliot.Adapters.LibraryAdapter;
import kris.hazen.dev.fulliot.Others.Plan;
import kris.hazen.dev.fulliot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LibraryAdapter adapter;
    private List<Plan> planList;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

       recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        planList = new ArrayList<>();
        adapter = new LibraryAdapter(getContext(), planList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

//        try {
//            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return rootView;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher};

        Plan a = new Plan("True Romance", 13, covers[0]);
        planList.add(a);

        a = new Plan("Xscpae", 8, covers[1]);
        planList.add(a);

        a = new Plan("Maroon 5", 11, covers[2]);
        planList.add(a);

        a = new Plan("Born to Die", 12, covers[3]);
        planList.add(a);

        a = new Plan("Honeymoon", 14, covers[4]);
        planList.add(a);

        a = new Plan("I Need a Doctor", 1, covers[5]);
        planList.add(a);

        a = new Plan("Loud", 11, covers[6]);
        planList.add(a);

        a = new Plan("Legend", 14, covers[7]);
        planList.add(a);

        a = new Plan("Hello", 11, covers[8]);
        planList.add(a);

        a = new Plan("Greatest Hits", 17, covers[9]);
        planList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
