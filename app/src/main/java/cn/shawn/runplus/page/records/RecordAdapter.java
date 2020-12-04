package cn.shawn.runplus.page.records;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cn.shawn.runplus.R;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.databinding.ItemRecordBinding;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.Holder> {

    private OnItemClickListener mListener;

    private List<RunRecord> mRecords;

    public RecordAdapter(OnItemClickListener listener, List<RunRecord> records) {
        this.mListener = listener;
        this.mRecords = records;
    }
    @NonNull
    @Override
    public RecordAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordAdapter.Holder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_record, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.Holder holder, int position) {
        holder.mDataBinding.setRecord(mRecords.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        ItemRecordBinding mDataBinding;

        OnItemClickListener mItemClickListener;

        public Holder(ItemRecordBinding binding, OnItemClickListener itemClickListener) {
            super(binding.getRoot());
            mDataBinding = binding;
            mItemClickListener = itemClickListener;
            itemView.setOnClickListener(v ->
                mItemClickListener.onItemClick(mDataBinding.getRecord()));
        }
    }

    public interface OnItemClickListener {

        void onItemClick(RunRecord record);
    }
}

