package com.cashproject.mongsil.databinding;
import com.cashproject.mongsil.R;
import com.cashproject.mongsil.BR;
import com.cashproject.mongsil.data.db.entity.Saying;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemDayBindingImpl extends ItemDayBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.constraintLayout, 2);
        sViewsWithIds.put(R.id.tv_day_date, 3);
        sViewsWithIds.put(R.id.tv_day_year, 4);
        sViewsWithIds.put(R.id.tv_day_month, 5);
        sViewsWithIds.put(R.id.iv_comment_icon, 6);
        sViewsWithIds.put(R.id.tv_comment_number, 7);
    }
    // views
    @NonNull
    private final com.cashproject.mongsil.util.SquareLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemDayBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private ItemDayBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[2]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[4]
            );
        this.ivDayImage.setTag(null);
        this.mboundView0 = (com.cashproject.mongsil.util.SquareLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.saying == variableId) {
            setSaying((Saying) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setSaying(@Nullable Saying Saying) {
        this.mSaying = Saying;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.saying);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        Saying saying = mSaying;
        java.lang.String sayingSquareImage = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (saying != null) {
                    // read saying.squareImage
                    sayingSquareImage = saying.getSquareImage();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.cashproject.mongsil.util.image.BindingAdapter.bindImage(this.ivDayImage, sayingSquareImage);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): saying
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}