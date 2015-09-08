package cellRenderers;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import spaningFeature.VerticalLabelUI;


public class VerticalTextCellRenderer extends DefaultTableCellRenderer {

    public VerticalTextCellRenderer() {
         setUI(new VerticalLabelUI(false));  //false de jos in sus; true invers :P
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         Component def = table.getDefaultRenderer(String.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         c.setBackground(def.getBackground());
         return c;
    }

}