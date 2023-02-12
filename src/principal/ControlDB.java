package principal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControlDB extends ConexionDB{

	public ControlDB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ResultSet consultarFacturas()
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
					  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
					  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
					  + "smn_comercial.smn_documento.smn_doc_gen_contable, "
					  + "smn_comercial.smn_factura_cabecera.smn_documento_id, "
					  + "case when smn_comercial.smn_factura_cabecera.smn_moneda_rf is null then 0 else smn_comercial.smn_factura_cabecera.smn_moneda_rf end, "
					  + "smn_cont_financiera.smn_rel_modulo_documento_tipo_doc.smn_tipo_documento_id, "
					  + "smn_comercial.smn_centro_facturacion.smn_clase_auxiliar_rf, "
					  + "smn_comercial.smn_centro_facturacion.smn_auxiliar_rf, "
					  +" smn_comercial.smn_pedido_cabecera.smn_cliente_id as smn_cliente_rf, "
					  + "smn_comercial.smn_documento.dcf_descripcion, "
					  + "Sum(smn_comercial.smn_pedido_cabecera.pca_monto_pedido_ml), "
					  + "Sum(smn_comercial.smn_pedido_cabecera.pca_monto_neto_ml), "
					  + "Sum(smn_comercial.smn_pedido_cabecera.pca_monto_pedido_ma), "
					  + "Sum(smn_comercial.smn_pedido_cabecera.pca_monto_neto_ma), "
					  + "sum(fca_monto_factura_ml) AS total_monto_factura_ml, "
					  + "sum(fca_monto_factura_ma) AS total_monto_factura_ma "
					  + "FROM "
					  + "smn_comercial.smn_factura_cabecera "
					  + "INNER JOIN smn_comercial.smn_documento ON smn_comercial.smn_factura_cabecera.smn_documento_id = smn_comercial.smn_documento.smn_documento_id "
					  + "INNER JOIN smn_cont_financiera.smn_rel_modulo_documento_tipo_doc ON smn_comercial.smn_documento.smn_doc_gen_contable = smn_cont_financiera.smn_rel_modulo_documento_tipo_doc.smn_documentos_generales_rf "
					  + "INNER JOIN smn_comercial.smn_rel_pedido_factura ON smn_comercial.smn_factura_cabecera.smn_factura_cabecera_id = smn_comercial.smn_rel_pedido_factura.smn_factura_cabecera_id "
					  + "INNER JOIN smn_comercial.smn_pedido_cabecera ON smn_comercial.smn_rel_pedido_factura.smn_pedido_cabecera_id = smn_comercial.smn_pedido_cabecera.smn_pedido_cabecera_id "
					  + "INNER JOIN smn_comercial.smn_centro_facturacion ON smn_comercial.smn_pedido_cabecera.smn_centro_facturacion_id = smn_comercial.smn_centro_facturacion.smn_centro_facturacion_id "
					  + "WHERE "
					  + "smn_comercial.smn_factura_cabecera.fca_estatus_proceso = 'GE' "
					  + "and "
					  + "fca_fecha_registro<current_date "
					  + "GROUP BY "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
					  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
					  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
					  + "smn_comercial.smn_documento.smn_doc_gen_contable, "
					  + "smn_comercial.smn_factura_cabecera.smn_documento_id, "
					  + "smn_comercial.smn_factura_cabecera.smn_moneda_rf, "
					  + "smn_cont_financiera.smn_rel_modulo_documento_tipo_doc.smn_tipo_documento_id, "
					  + "smn_comercial.smn_centro_facturacion.smn_clase_auxiliar_rf, "
					  + "smn_comercial.smn_centro_facturacion.smn_auxiliar_rf, "
					  +" smn_comercial.smn_pedido_cabecera.smn_cliente_id, "
					  + "smn_comercial.smn_documento.dcf_descripcion "
					  + "ORDER BY "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro ASC ";
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarDocGeneral(int smn_documento_id)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT smn_documentos_generales_rf "
			  + "FROM smn_comercial.smn_documento "
			  + "WHERE "
			  + "smn_documento_id="+smn_documento_id;
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarNumero_ccc()
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT ccc_numero_documento "
			  + "FROM smn_cont_financiera.smn_control_cierre_contable "
			  + "ORDER BY ccc_numero_documento DESC LIMIT 1";
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet insertControl_cierre(int smn_entidad_rf, int smn_sucursal_rf, int smn_documento_rf, int smn_documento_id, Date ccc_fecha, int ccc_numero_documento, double ccc_monto_ml, double ccc_monto_ma, int smn_moneda_rf, int smn_tasa, String ccc_estatus, String ccc_idioma, String ccc_usuario, Date ccc_fecha_registro, String ccc_hora, int smn_modulo_rf,int smn_clase_auxiliar_rf,int smn_auxiliar_rf)
	{
    	String sql;
    	ResultSet rs=null;
    	PreparedStatement stmt;
    	
		try {
			sql="INSERT INTO smn_cont_financiera.smn_control_cierre_contable "
			  + "(smn_control_cierre_contable_id, smn_entidad_rf, smn_sucursal_rf, smn_documento_rf, smn_documento_id, ccc_fecha, ccc_numero_documento, ccc_monto_ml, ccc_monto_ma, smn_moneda_rf, smn_tasa, ccc_estatus, ccc_idioma, ccc_usuario, ccc_fecha_registro, ccc_hora, smn_modulo_rf,smn_clase_auxiliar_rf,smn_auxiliar_rf) "
			  + "VALUES (nextval('smn_cont_financiera.seq_smn_control_cierre_contable'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING smn_control_cierre_contable_id";
					
			stmt = this.cn.prepareStatement(sql);
			stmt.setInt(1, smn_entidad_rf);
			stmt.setInt(2, smn_sucursal_rf);
			stmt.setInt(3, smn_documento_rf);
			stmt.setInt(4, smn_documento_id);
			stmt.setDate(5, ccc_fecha);
			stmt.setInt(6, ccc_numero_documento);
			stmt.setDouble(7, ccc_monto_ml);
			stmt.setDouble(8, ccc_monto_ma);
			stmt.setInt(9, smn_moneda_rf);
			stmt.setInt(10, smn_tasa);
			stmt.setString(11, ccc_estatus);
			stmt.setString(12, ccc_idioma);
			stmt.setString(13, ccc_usuario);
			stmt.setDate(14, ccc_fecha_registro);
			stmt.setString(15, ccc_hora);
			stmt.setInt(16, smn_modulo_rf);
			stmt.setInt(17, smn_clase_auxiliar_rf);
			stmt.setInt(18, smn_auxiliar_rf);
			
			rs=stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public void updateFacturaCabecera(int smn_entidad_rf, int smn_sucursal_rf, int smn_documento_id, int smn_moneda_rf, Date fca_fecha_registro, int smn_control_cierre_contable_id)
	{
    	String sql;
    	PreparedStatement stmt;
    	
		try {
			sql="UPDATE smn_comercial.smn_factura_cabecera SET "
			  + "smn_control_cierre_contable_id=? "
			  + "WHERE "
			  + "smn_entidad_rf=? "
			  + "AND "
			  + "smn_sucursal_rf=? "
			  + "AND "
			  + "smn_documento_id=? "
			  + "AND "
			  + "smn_moneda_rf=? "
			  + "AND "
			  + "fca_fecha_registro=? "
			  + "AND "
			  + "fca_estatus_proceso='GE'";
					
			stmt = this.cn.prepareStatement(sql);
			stmt.setInt(1, smn_control_cierre_contable_id);
			stmt.setInt(2, smn_entidad_rf);
			stmt.setInt(3, smn_sucursal_rf);
			stmt.setInt(4, smn_documento_id);
			stmt.setInt(5, smn_moneda_rf);
			stmt.setDate(6, fca_fecha_registro);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public ResultSet moduloComercial()
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT smn_modulos_id "
			  + "FROM smn_base.smn_modulos "
			  + "WHERE "
			  + "mod_codigo='SMN_CME'";
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet tipoDocumento(int smn_modulo_rf, int smn_documentos_generales_rf)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT smn_tipo_documento_id, smn_rel_modulo_documento_tipo_doc_id "
			  + "FROM smn_cont_financiera.smn_rel_modulo_documento_tipo_doc "
			  + "WHERE "
			  + "smn_modulos_rf="+smn_modulo_rf
			  + " AND "
			  + "smn_documentos_generales_rf="+smn_documentos_generales_rf;
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet numeroDocumento()
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT doc_numero_documento "
			  + "FROM smn_cont_financiera.smn_documento "
			  + "ORDER BY doc_numero_documento DESC LIMIT 1";
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet tipoComprobante(int smn_rel_modulo_documento_tipo_doc_id)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT mcc_tipo_comp "
			  + "FROM smn_cont_financiera.smn_modelo_comprobante "
			  + "WHERE "
			  + "mcc_documento="+smn_rel_modulo_documento_tipo_doc_id;
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet insertDocumento(int smn_modulo_rf, int smn_entidades_rf, int smn_sucursales_rf, 
			int smn_documentos_generales_rf, int smn_tipo_documento_id, int doc_numero_documento,
			int smn_clase_auxiliar_rf, int smn_auxiliar_rf, int smn_clase_auxiliar_ord_rf, 
			int smn_auxiliar_ord_rf, int doc_orden_compra_rf, int smn_centro_costo_rf, int smn_proyecto_rf, 
			Date doc_fecha_doc, Date doc_fecha_rec, Date doc_fecha_vcto, String doc_planilla_importacion,
			double doc_monto_ml, double doc_monto_me, double doc_tasa_cambio, int smn_documentos_generales_rf_afecta,
			int doc_numero_doc_afecta, int doc_numero_control_doc_afect, Date doc_fecha_doc_afecta, int smn_codigos_impuestos_rf,
			int doc_numero_control_fiscal_inicial, int doc_numero_control_fiscal_ultimo, int doc_numero_control1_inicial,
			int doc_numero_control1_ultimo, int doc_numero_control2_inicial, int doc_numero_control2_ultimo,
			String doc_estatus, int doc_ano_comprobante, int doc_periodos_detalles_rf, int smn_tipo_comprobante_id,
			int doc_num_comprobante, String doc_idioma, String doc_usuario, Date doc_fecha_registro, String doc_hora,
			Date doc_fecha_comprobante, String doc_numero_control, int smn_elemento_rf, String doc_descripcion, int smn_moneda_rf)
	{
		String sql;
		PreparedStatement stmt;
    	ResultSet rs=null;
    	
		try {
			sql="INSERT INTO smn_cont_financiera.smn_documento "
		      + "(smn_documento_id, smn_modulo_rf, smn_entidades_rf, smn_sucursales_rf, smn_documentos_generales_rf, smn_tipo_documento_id, doc_numero_documento, smn_clase_auxiliar_rf, smn_auxiliar_rf, smn_clase_auxiliar_ord_rf, smn_auxiliar_ord_rf, doc_orden_compra_rf, smn_centro_costo_rf, smn_proyecto_rf, doc_fecha_doc, doc_fecha_rec, doc_fecha_vcto, doc_planilla_importacion, doc_monto_ml, doc_monto_me, doc_tasa_cambio, smn_documentos_generales_rf_afecta, doc_numero_doc_afecta, doc_numero_control_doc_afect, doc_fecha_doc_afecta, smn_codigos_impuestos_rf, doc_numero_control_fiscal_inicial, doc_numero_control_fiscal_ultimo, doc_numero_control1_inicial, doc_numero_control1_ultimo, doc_numero_control2_inicial, doc_numero_control2_ultimo, doc_estatus, doc_ano_comprobante, doc_periodos_detalles_rf, smn_tipo_comprobante_id, doc_num_comprobante, doc_idioma, doc_usuario, doc_fecha_registro, doc_hora, doc_fecha_comprobante, doc_numero_control, smn_elemento_rf, doc_descripcion, smn_moneda_rf) "
		      + "VALUES (nextval('smn_cont_financiera.seq_smn_documento'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING smn_documento_id AS smn_documento_id_cont";
			
			stmt = this.cn.prepareStatement(sql);
			stmt.setInt(1, smn_modulo_rf);
			stmt.setInt(2, smn_entidades_rf);
			stmt.setInt(3, smn_sucursales_rf);
			stmt.setInt(4, smn_documentos_generales_rf);
			stmt.setInt(5, smn_tipo_documento_id);
			stmt.setInt(6, doc_numero_documento);
			stmt.setInt(7, smn_clase_auxiliar_rf);
			stmt.setInt(8, smn_auxiliar_rf);
			stmt.setInt(9, smn_clase_auxiliar_ord_rf);
			stmt.setInt(10, smn_auxiliar_ord_rf);
			stmt.setInt(11, doc_orden_compra_rf);
			stmt.setInt(12, smn_centro_costo_rf);
			stmt.setInt(13, smn_proyecto_rf); 
			stmt.setDate(14, doc_fecha_doc);
			stmt.setDate(15, doc_fecha_rec);
			stmt.setDate(16, doc_fecha_vcto);
			stmt.setString(17, doc_planilla_importacion);
			stmt.setDouble(18, doc_monto_ml);
			stmt.setDouble(19, doc_monto_me);
			stmt.setDouble(20, doc_tasa_cambio);
			stmt.setInt(21, smn_documentos_generales_rf_afecta);
			stmt.setInt(22, doc_numero_doc_afecta);
			stmt.setInt(23, doc_numero_control_doc_afect);
			stmt.setDate(24, doc_fecha_doc_afecta);
			stmt.setInt(25, smn_codigos_impuestos_rf);
			stmt.setInt(26, doc_numero_control_fiscal_inicial);
			stmt.setInt(27, doc_numero_control_fiscal_ultimo);
			stmt.setInt(28, doc_numero_control1_inicial);
			stmt.setInt(29, doc_numero_control1_ultimo);
			stmt.setInt(30, doc_numero_control2_inicial);
			stmt.setInt(31, doc_numero_control2_ultimo);
			stmt.setString(32, doc_estatus);
			stmt.setInt(33, doc_ano_comprobante);
			stmt.setInt(34, doc_periodos_detalles_rf);
			stmt.setInt(35, smn_tipo_comprobante_id);
			stmt.setInt(36, doc_num_comprobante);
			stmt.setString(37, doc_idioma);
			stmt.setString(38, doc_usuario);
			stmt.setDate(39, doc_fecha_registro);
			stmt.setString(40, doc_hora);
			stmt.setDate(41, doc_fecha_comprobante);
			stmt.setString(42, doc_numero_control);
			stmt.setInt(43, smn_elemento_rf);
			stmt.setString(44, doc_descripcion);
			stmt.setInt(45, smn_moneda_rf);
			rs=stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet consultarDetalles(int smn_entidad_rf, int smn_sucursal_rf, int smn_documento_id, int smn_moneda_rf, Date fca_fecha_registro)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
					  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
					  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
					  + "smn_comercial.smn_documento.smn_doc_gen_contable, "
					  + "smn_comercial.smn_pedido_componentes.smn_grupo_componente_rf, "
					  + "smn_base.smn_grupo_componente.gco_descripcion, "
					  + "smn_base.smn_rel_servicio_area_unidad.smn_centro_costo_rf, "
					  + "Sum(smn_comercial.smn_pedido_componentes.pco_monto_ml), "
					  + "Sum(smn_comercial.smn_pedido_componentes.smn_monto_ma), "
					  + "smn_comercial.smn_pedido_componentes.pco_tipo_componente, "
					  + "smn_comercial.smn_pedido_componentes.smn_componente_rf, "
					  + "smn_comercial.smn_pedido_componentes.smn_clase_auxiliar_rf, "
					  + "smn_comercial.smn_pedido_componentes.smn_auxiliar_rf, "
					  + "case "
					  + "when cmp_tipo_componente = 'IT' then (select smn_centro_costo_rf from smn_inventario.smn_caracteristica_item where smn_item_rf=smn_base.smn_componentes.smn_item_rf) "
					  + "else (COALESCE((select smn_centro_costo_rf from smn_base.smn_rel_servicio_area_unidad where smn_servicios_id=smn_comercial.smn_pedido_componentes.smn_servicio_rf AND smn_areas_servicios_rf=smn_comercial.smn_pedido_cabecera.smn_area_servicio_rf AND smn_unidades_servicios_rf = smn_comercial.smn_pedido_cabecera.smn_unidad_servicio_rf),0))"
					  + "end as centro_costo, "
					  + "sum(pco_monto_ml) AS total_monto_ml, "
					  + "sum(smn_monto_ma) AS total_monto_ma "
					  + "FROM "
					  + "smn_comercial.smn_factura_cabecera "
					  + "INNER JOIN smn_comercial.smn_rel_pedido_factura ON smn_comercial.smn_factura_cabecera.smn_factura_cabecera_id = smn_comercial.smn_rel_pedido_factura.smn_factura_cabecera_id "
					  + "INNER JOIN smn_comercial.smn_pedido_cabecera ON smn_comercial.smn_rel_pedido_factura.smn_pedido_cabecera_id = smn_comercial.smn_pedido_cabecera.smn_pedido_cabecera_id "
					  + "INNER JOIN smn_comercial.smn_pedido_detalle ON smn_comercial.smn_pedido_cabecera.smn_pedido_cabecera_id = smn_comercial.smn_pedido_detalle.smn_pedido_cabecera_id "
					  + "INNER JOIN smn_comercial.smn_pedido_componentes ON smn_comercial.smn_pedido_cabecera.smn_pedido_cabecera_id = smn_comercial.smn_pedido_componentes.smn_pedido_cabecera_id AND smn_comercial.smn_pedido_detalle.smn_pedido_detalle_id = smn_comercial.smn_pedido_componentes.smn_pedido_detalle_id "
					  + "INNER JOIN smn_salud.smn_ingresos ON smn_comercial.smn_pedido_componentes.smn_ingresos_rf = smn_salud.smn_ingresos.igs_num_ingreso AND smn_comercial.smn_pedido_cabecera.smn_entidad_rf = smn_salud.smn_ingresos.smn_entidades_rf AND smn_comercial.smn_pedido_cabecera.smn_sucursal_rf = smn_salud.smn_ingresos.smn_sucursales_rf AND smn_comercial.smn_pedido_cabecera.smn_area_servicio_rf = smn_salud.smn_ingresos.smn_areas_servicios_rf "
					  + "LEFT JOIN smn_salud.smn_ingreso_movimiento ON smn_salud.smn_ingresos.smn_ingresos_id = smn_salud.smn_ingreso_movimiento.smn_ingreso_id AND smn_comercial.smn_pedido_detalle.smn_servicios_rf = smn_salud.smn_ingreso_movimiento.smn_servicios_rf "
					  + "INNER JOIN smn_base.smn_rel_servicio_area_unidad ON smn_salud.smn_ingresos.smn_areas_servicios_rf = smn_base.smn_rel_servicio_area_unidad.smn_areas_servicios_rf AND smn_salud.smn_ingreso_movimiento.smn_unidades_servicios_rf = smn_base.smn_rel_servicio_area_unidad.smn_unidades_servicios_rf AND smn_salud.smn_ingreso_movimiento.smn_servicios_rf = smn_base.smn_rel_servicio_area_unidad.smn_servicios_id "
					  + "INNER JOIN smn_base.smn_grupo_componente ON smn_comercial.smn_pedido_componentes.smn_grupo_componente_rf = smn_base.smn_grupo_componente.smn_grupo_componente_id "
					  + "INNER JOIN smn_comercial.smn_documento ON smn_comercial.smn_factura_cabecera.smn_documento_id = smn_comercial.smn_documento.smn_documento_id "
					  + "INNER JOIN smn_base.smn_componentes on smn_base.smn_componentes.smn_componentes_id=smn_comercial.smn_pedido_componentes.smn_componente_rf "
					  + "WHERE "
					  + "smn_comercial.smn_factura_cabecera.fca_estatus_proceso = 'GE' "
					  + "AND "
					  + "fca_fecha_registro='"+fca_fecha_registro+"'"
					  + " AND "
					  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf="+smn_entidad_rf
					  + " AND "
					  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf="+smn_sucursal_rf
					  + " AND "
					  + "smn_comercial.smn_factura_cabecera.smn_documento_id="+smn_documento_id
					  + " AND "
					  + "smn_comercial.smn_factura_cabecera.smn_moneda_rf="+smn_moneda_rf+" "
					  + "GROUP BY "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
					  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
					  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
					  + "smn_comercial.smn_documento.smn_doc_gen_contable, "
					  + "smn_comercial.smn_pedido_componentes.smn_grupo_componente_rf, "
					  + "smn_base.smn_rel_servicio_area_unidad.smn_centro_costo_rf, "
					  + "smn_base.smn_grupo_componente.gco_descripcion, "
					  + "smn_comercial.smn_pedido_componentes.pco_tipo_componente, "
					  + "smn_comercial.smn_pedido_componentes.smn_componente_rf, "
					  + "centro_costo, "
					  + "smn_comercial.smn_pedido_componentes.smn_clase_auxiliar_rf, "
					  + "smn_comercial.smn_pedido_componentes.smn_auxiliar_rf "
					  + "ORDER BY "
					  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro ASC ";
			System.out.println(sql);
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarDiccionario(String cmp_tipo_componente)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	String campo=null;
    	
		try {
			if(cmp_tipo_componente.equals("IT"))
				campo="smn_item_rf";
			else
			if(cmp_tipo_componente.equals("SE"))
				campo="smn_servicio_rf";
			else
				campo="smn_componente_rf";
			
			sql="SELECT smn_diccionario_id "
			  + "FROM smn_base.smn_diccionario "
			  + "WHERE "
			  + "dic_esquema='smn_comercial'"
			  + " AND "
			  + "(dic_tabla='smn_pedido_componentes' OR dic_tabla = 'smn_pedido_detalle')"
			  + " AND "
			  + "dic_campo='"+campo+"'";
			
			stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarTipoElemento(int smn_diccionario_rf, int smn_modulo_rf)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;
    	
		try {
			sql="SELECT smn_tipo_elemento_id "
			  + "FROM smn_cont_financiera.smn_tipo_elemento "
			  + "WHERE "
			  + "smn_diccionario_rf="+smn_diccionario_rf
			  + " AND "
			  + "smn_modulo_rf="+smn_modulo_rf;
			
			stmt = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarComponente(int smn_componente_rf)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;

		try {
			sql="SELECT "
			  + "cmp_descripcion "
			  + "FROM smn_base.smn_componentes "
			  + "WHERE "
			  + "smn_componentes_id="+smn_componente_rf;
		
			  stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public void insertDocumentoDetalle(int smn_documento_id, int smn_tipo_elemento_id, 
			int smn_elemento_id, int smn_clase_auxiliar_rf, int smn_auxiliar_rf, int smn_proyecto_rf,
			int smn_centro_costo_rf, double dod_monto_ml, int smn_moneda_rf, int smn_tasa_cambio_rf,
			double dod_monto_ma, String dod_estatus, String dod_idioma, String dod_usuario,
			Date dod_fecha_registro, String dod_hora, double dod_monto_neto_ml, double dod_monto_neto_ma,
			String dod_descripcion_elemento)
	{
		String sql;
		PreparedStatement stmt;
    	
		try {
			sql="INSERT INTO smn_cont_financiera.smn_documento_detalle "
			  + "(smn_documento_detalle_id, smn_documento_id, smn_tipo_elemento_id, smn_elemento_id, smn_clase_auxiliar_rf, smn_auxiliar_rf, smn_proyecto_rf, smn_centro_costo_rf, dod_monto_ml, smn_moneda_rf, smn_tasa_cambio_rf, dod_monto_ma, dod_estatus, dod_idioma, dod_usuario, dod_fecha_registro, dod_hora, dod_monto_neto_ml, dod_monto_neto_ma, dod_descripcion_elemento) "
			  + "VALUES (nextval('smn_cont_financiera.seq_smn_documento_detalle'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			stmt = cn.prepareStatement(sql);
			stmt.setInt(1, smn_documento_id);
			stmt.setInt(2, smn_tipo_elemento_id);
			stmt.setInt(3, smn_elemento_id);
			stmt.setInt(4, smn_clase_auxiliar_rf);
			stmt.setInt(5, smn_auxiliar_rf);
			stmt.setInt(6, smn_proyecto_rf);
			stmt.setInt(7, smn_centro_costo_rf);
			stmt.setDouble(8, dod_monto_ml);
			stmt.setInt(9, smn_moneda_rf);
			stmt.setInt(10, smn_tasa_cambio_rf);
			stmt.setDouble(11, dod_monto_ma);
			stmt.setString(12, dod_estatus);
			stmt.setString(13, dod_idioma);
			stmt.setString(14, dod_usuario);
			stmt.setDate(15, dod_fecha_registro);
			stmt.setString(16, dod_hora);
			stmt.setDouble(17, dod_monto_neto_ml);
			stmt.setDouble(18, dod_monto_neto_ma);
			stmt.setString(19, dod_descripcion_elemento);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateFactura(String fca_estatus_proceso, int smn_entidad_rf, int smn_sucursal_rf,
			int smn_documento_id, int smn_moneda_rf, Date fca_fecha_registro)
	{
		String sql;
		PreparedStatement stmt;
	   	
		try {
			sql="UPDATE smn_comercial.smn_factura_cabecera SET "
			  + "fca_estatus_proceso=? "
			  + "WHERE "
			  + "smn_entidad_rf=? "
			  + "AND "
			  + "smn_sucursal_rf=? "
			  + "AND "
			  + "smn_documento_id=? "
			  + "AND "
			  + "smn_moneda_rf=? "
			  + "AND "
			  + "fca_fecha_registro=? "
			  + "AND "
			  + "fca_estatus_proceso='GE'";
				
			stmt = this.cn.prepareStatement(sql);
			stmt.setString(1, fca_estatus_proceso);
			stmt.setInt(2, smn_entidad_rf);
			stmt.setInt(3, smn_sucursal_rf);
			stmt.setInt(4, smn_documento_id);
			stmt.setInt(5, smn_moneda_rf);
			stmt.setDate(6, fca_fecha_registro);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet facturas_contabilizadas(int smn_entidad_rf, int smn_sucursal_rf, int smn_documento_id,
			int smn_moneda_rf, Date fca_fecha_registro)
	{
		String sql;
	    ResultSet rs=null;
	    Statement stmt;
	    
		try {
			sql="SELECT smn_factura_cabecera_id "
			  + "FROM smn_comercial.smn_factura_cabecera "
			  + "WHERE "
			  + "smn_entidad_rf="+smn_entidad_rf
			  + " AND "
			  + "smn_sucursal_rf="+smn_sucursal_rf
			  + " AND "
			  + "smn_documento_id="+smn_documento_id
			  + " AND "
			  + "smn_moneda_rf="+smn_moneda_rf
			  + " AND "
			  + "fca_fecha_registro='"+fca_fecha_registro+"'"
			  + " AND "
			  + "fca_estatus_proceso='CO'";
			stmt = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return rs;
	}
	
	public ResultSet consultarAuxiliarCliente(int smn_cliente_id)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;

		try {
			sql="SELECT "
			  + "smn_auxiliar_rf, "
			  + "smn_clase_auxiliar_rf "
			  + "FROM smn_comercial.smn_cliente "
			  + "WHERE "
			  + "smn_cliente_id="+smn_cliente_id;
		
			  stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarFactura(int smn_factura_cabecera_id)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;

		try {
			sql="SELECT "
			  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
			  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
			  + "smn_comercial.smn_factura_cabecera.smn_documento_id, "
			  + "smn_comercial.smn_factura_cabecera.smn_cliente_id, "
			  + "smn_comercial.smn_factura_cabecera.fca_rif, "
			  + "smn_comercial.smn_factura_cabecera.fca_numero_documento, "
			  + "smn_comercial.smn_factura_cabecera.fca_numero_control_fiscal, "
			  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
			  + "SUM(smn_comercial.smn_factura_impresion_detalle.fim_monto_ml) AS fim_monto_ml, "
			  + "SUM(smn_comercial.smn_factura_impresion_detalle.fim_monto_ma) AS fim_monto_ma, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_codigos_impuestos_rf, "
			  + "smn_base.smn_codigos_impuestos.imp_porcentaje_calculo, "
			  + "SUM(smn_comercial.smn_factura_impresion_detalle.fim_monto_impuesto_ml) AS fim_monto_impuesto_ml, "
			  + "smn_base.smn_codigos_impuestos.imp_monto_sustraendo, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_moneda_rf, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_tasa_rf, "
			  + "SUM(smn_comercial.smn_factura_impresion_detalle.fim_monto_impuesto_ma) AS fim_monto_impuesto_ma, "
			  + "smn_base.smn_grupo_titulo_impresion.gti_venta_terceros "
			  + "FROM "
			  + "smn_comercial.smn_factura_cabecera "
			  + "INNER JOIN smn_comercial.smn_rel_pedido_factura ON smn_comercial.smn_factura_cabecera.smn_factura_cabecera_id = smn_comercial.smn_rel_pedido_factura.smn_factura_cabecera_id "
			  + "INNER JOIN smn_comercial.smn_factura_impresion_detalle ON smn_comercial.smn_rel_pedido_factura.smn_pedido_cabecera_id = smn_comercial.smn_factura_impresion_detalle.smn_pedido_cabecera_id "
			  + "INNER JOIN smn_base.smn_grupo_titulo_impresion ON smn_base.smn_grupo_titulo_impresion.smn_grupo_titulo_impresion_id = smn_comercial.smn_factura_impresion_detalle.smn_grupo_titulo_impresion_rf "
			  + "INNER JOIN smn_base.smn_codigos_impuestos ON smn_base.smn_codigos_impuestos.smn_codigos_impuestos_id = smn_comercial.smn_factura_impresion_detalle.smn_codigos_impuestos_rf "
			  + "WHERE "
			  + "smn_comercial.smn_factura_cabecera.smn_factura_cabecera_id="+smn_factura_cabecera_id+" "
			  + "GROUP BY "
			  + "smn_comercial.smn_factura_cabecera.smn_entidad_rf, "
			  + "smn_comercial.smn_factura_cabecera.smn_sucursal_rf, "
			  + "smn_comercial.smn_factura_cabecera.smn_documento_id, "
			  + "smn_comercial.smn_factura_cabecera.smn_cliente_id, "
			  + "smn_comercial.smn_factura_cabecera.fca_rif, "
			  + "smn_comercial.smn_factura_cabecera.fca_numero_documento, "
			  + "smn_comercial.smn_factura_cabecera.fca_numero_control_fiscal, "
			  + "smn_comercial.smn_factura_cabecera.fca_fecha_registro, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_codigos_impuestos_rf, "
			  + "smn_base.smn_codigos_impuestos.imp_porcentaje_calculo, "
			  + "smn_base.smn_codigos_impuestos.imp_monto_sustraendo, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_moneda_rf, "
			  + "smn_comercial.smn_factura_impresion_detalle.smn_tasa_rf, "
			  + "smn_base.smn_grupo_titulo_impresion.gti_venta_terceros";
		
			  stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public ResultSet consultarCodigoImpuesto(int smn_codigos_impuestos_id)
	{
    	String sql;
    	ResultSet rs=null;
    	Statement stmt;

		try {
			sql="SELECT "
			  + "imp_porcentaje_base "
			  + "FROM smn_base.smn_codigos_impuestos "
			  + "WHERE "
			  + "smn_codigos_impuestos_id="+smn_codigos_impuestos_id;
		
			  stmt = this.cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return rs;
    }
	
	public void insertImpuesto(int smn_entidad_rf, int smn_sucursal_rf, int smn_documento_id, 
			int smn_modulo_rf, int smn_clase_auxiliar_rf, int smn_auxiliar_rf, 
			String fca_rif, int fca_numero_documento, int fca_numero_control_fiscal, 
			Date fca_fecha_registro, double imp_porcentaje_base, double fim_monto_ml, 
			String gti_venta_terceros, int smn_codigos_impuestos_rf, int imp_porcentaje_calculo,
			double fim_monto_impuesto_ml, double imp_monto_sustraendo, double imp_monto_neto_impuesto,  
			String imp_idioma, String imp_usuario, Date imp_fecha_registro, String imp_hora,
			int smn_moneda_rf, int smn_tasa_rf, double fim_monto_impuesto_ma, double fim_monto_ma, double imp_monto_neto_impuesto_ma)
	{
		String sql;
		PreparedStatement stmt;
		
		try {
			sql="INSERT INTO smn_impuestos.smn_impuestos "
			  + "(smn_impuestos_id, "
			  + "smn_entidades_id, "
			  + "smn_auxiliar_sucursales_id, "
			  + "imp_documento_origen, "
			  + "imp_modulo_origen_id, "
			  + "smn_clase_auxiliar_id, "
			  + "smn_auxiliar_id, "
			  + "imp_rif, "
			  + "imp_numero_documento, "
			  + "imp_num_control_fiscal, "
			  + "imp_fecha, "
			  + "imp_base_imponible_porcentaje, "
			  + "imp_base_imponible_monto, "
			  + "smn_codigo_impuesto_rf, "
			  + "imp_porcentaje, "
			  + "imp_monto_impuesto, "
			  + "imp_sustraendo, "
			  + "imp_monto_neto_impuesto, "
			  + "imp_descripcion, "
			  + "imp_cantidad, "
			  + "imp_estatus_comprobante, "
			  + "imp_estatus, "
			  + "imp_idioma, "
			  + "imp_usuario_id, "
			  + "imp_fecha_registro, "
			  + "imp_hora, "
			  + "imp_control_final, "
			  + "imp_num_factura_final, "
			  + "smn_monedas_rf, "
			  + "smn_tasa_cambio_rf, "
			  + "imp_monto_neto_impuesto_ma, "
			  + "imp_monto_venta_terceros_ml, "
			  + "imp_monto_venta_terceros_ma, "
			  + "imp_impuesto_venta_tercero_ml, "
			  + "imp_impuesto_venta_tercero_ma) "
			  + "VALUES (nextval('smn_impuestos.seq_smn_impuestos'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			stmt = cn.prepareStatement(sql);
			
			stmt.setInt(1, smn_entidad_rf);
			stmt.setInt(2, smn_sucursal_rf);
			stmt.setInt(3, smn_documento_id);
			stmt.setInt(4, smn_modulo_rf);
			stmt.setInt(5, smn_clase_auxiliar_rf);
			stmt.setInt(6, smn_auxiliar_rf);
			stmt.setString(7, fca_rif);
			stmt.setInt(8, fca_numero_documento);
			stmt.setInt(9, fca_numero_control_fiscal);
			stmt.setDate(10, fca_fecha_registro);
			stmt.setDouble(11, imp_porcentaje_base);
			stmt.setInt(13, smn_codigos_impuestos_rf);
			stmt.setInt(14, imp_porcentaje_calculo);
			stmt.setDouble(15, fim_monto_impuesto_ml);
			stmt.setDouble(16, imp_monto_sustraendo);
			stmt.setDouble(17, imp_monto_neto_impuesto);
			stmt.setString(18, "Ventas Diarias");
			stmt.setInt(19, 1);
			stmt.setString(20, "CO");
			stmt.setString(21, "RE");
			stmt.setString(22, imp_idioma);
			stmt.setString(23, imp_usuario);
			stmt.setDate(24, imp_fecha_registro);
			stmt.setString(25, imp_hora);
			stmt.setInt(26, fca_numero_control_fiscal);
			stmt.setInt(27, fca_numero_documento);
			stmt.setInt(28, smn_moneda_rf);
			stmt.setInt(29, smn_tasa_rf);
			stmt.setDouble(30, imp_monto_neto_impuesto_ma);
			
			if(gti_venta_terceros.equals("NO"))
			{
				stmt.setDouble(12, fim_monto_ml);
				stmt.setInt(31, 0);
				stmt.setInt(32, 0);
				stmt.setInt(33, 0);
				stmt.setInt(34, 0);
			}
			else
			{
				stmt.setDouble(12, 0);
				stmt.setDouble(31, fim_monto_ml);
				stmt.setDouble(32, fim_monto_ma);
				stmt.setDouble(33, fim_monto_impuesto_ml);
				stmt.setDouble(34, fim_monto_impuesto_ma);
			}
			
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
