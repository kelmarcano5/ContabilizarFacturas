package principal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		int result=0;
		ControlDB control = new ControlDB();
		control.cn.setAutoCommit(false);
		
		int smn_documento_id;
		int smn_entidad_rf;
		int smn_sucursal_rf;
		int smn_moneda_rf;
		int smn_documentos_generales_rf;
		int ccc_numero_documento;
		int smn_control_cierre_contable_id;
		int smn_clase_auxiliar_rf;
		int smn_auxiliar_rf;
		int smn_tasa;
		int smn_modulo_rf;
		int smn_tipo_documento_id;
		int smn_rel_modulo_documento_tipo_doc_id;
		int doc_numero_documento;
		int smn_clase_auxiliar_ord_rf;
		int smn_auxiliar_ord_rf;
		int doc_orden_compra_rf;
		int smn_centro_costo_rf;
		int smn_proyecto_rf;
		int smn_documentos_generales_rf_afecta;
		int doc_numero_doc_afecta;
		int doc_numero_control_doc_afect;
		int smn_codigos_impuestos_rf;
		int doc_numero_control_fiscal_inicial;
		int doc_numero_control_fiscal_ultimo;
		int doc_numero_control1_inicial;
		int doc_numero_control1_ultimo;
		int doc_numero_control2_inicial;
		int doc_numero_control2_ultimo;
		int doc_ano_comprobante;
		int doc_periodos_detalles_rf;
		int smn_tipo_comprobante_id;
		int doc_num_comprobante;
		int smn_elemento_rf;
		int smn_documento_id_cont;
		int smn_componente_rf;
		int smn_diccionario_id;
		int smn_tipo_elemento_id;
		int smn_tasa_rf;
		double fca_monto_factura_ml=0;
		double fca_monto_factura_ma=0;
		double doc_tasa_cambio;
		double total_monto_ml;
		double total_monto_ma;
		double dod_monto_neto_ml;
		double dod_monto_neto_ma;
		java.sql.Date fca_fecha_registro=null;
		//java.sql.Date doc_fecha_doc;
		java.sql.Date doc_fecha_rec;
		java.sql.Date doc_fecha_vcto;
		java.sql.Date doc_fecha_doc_afecta;
		java.sql.Date doc_fecha_comprobante;
		String ccc_estatus;
		String ccc_idioma;
		String ccc_usuario;
		String doc_estatus;
		String doc_planilla_importacion;
		String doc_numero_control;
		String doc_descripcion;
		String pco_tipo_componente;
		String dcf_descripcion;
		String cmp_descripcion;
		String dod_estatus;
		
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual= sdformat.format(new Date());
		
		String sistemaOperativo = System.getProperty("os.name");
		String file;
		  
		if(sistemaOperativo.equals("Windows 7") || sistemaOperativo.equals("Windows 8") || sistemaOperativo.equals("Windows 10")) 
			file =  "C:/log/logContabilizarFacturas_"+fechaActual+".txt";
		else
			file = "./logContabilizarFacturas_"+fechaActual+".txt";
		
		File newLogFile = new File(file);
		FileWriter fw;
		String str="";
		
		if(!newLogFile.exists())
			fw = new FileWriter(newLogFile);
		else
			fw = new FileWriter(newLogFile,true);
		
		BufferedWriter bw=new BufferedWriter(fw);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = date.parse(fechaActual);
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        
        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        int seconds = locaDate.getSecond();
        String hora = hours+":"+minutes+":"+seconds;
        
		try
		{
			str = "----------"+timestamp+"----------";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			bw.newLine();
			
			str = "-- INICIO DEL PROCESO: 'Contabilizar Facturas' --";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			str = "<<Consultando facturas para generar cierre contable...";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			ResultSet facturas=control.consultarFacturas();
			
			if(getRecordCount(facturas)>0)
			{
				
				while(facturas.next())
				{	
					fca_fecha_registro=facturas.getDate("fca_fecha_registro");
					smn_entidad_rf=facturas.getInt("smn_entidad_rf");
					smn_sucursal_rf=facturas.getInt("smn_sucursal_rf");					
					smn_documento_id=facturas.getInt("smn_documento_id");
					smn_moneda_rf=facturas.getInt("smn_moneda_rf");
					smn_clase_auxiliar_rf=facturas.getInt("smn_clase_auxiliar_rf");
					smn_auxiliar_rf=facturas.getInt("smn_auxiliar_rf");
					fca_monto_factura_ml=facturas.getDouble("total_monto_factura_ml");
					fca_monto_factura_ma=facturas.getDouble("total_monto_factura_ma");
					dcf_descripcion= facturas.getString("dcf_descripcion");
					
					ResultSet documento_general=control.consultarDocGeneral(smn_documento_id);
					
					if(getRecordCount(documento_general)>0)
					{
						documento_general.next();
						if(documento_general.getString("smn_documentos_generales_rf")!=null)
						{
							smn_documentos_generales_rf=documento_general.getInt("smn_documentos_generales_rf");
						}	
						else
						{
							str = "*El documento general esta vacio*";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							result=1;
							break;
						}
					}
					else
					{
						str = "*No se encontro el documento de la factura*";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					ResultSet numero_cierre=control.consultarNumero_ccc();
					
					if(getRecordCount(numero_cierre)>0)
					{
						numero_cierre.next();
						if(numero_cierre.getString("ccc_numero_documento")!=null)
							ccc_numero_documento=numero_cierre.getInt("ccc_numero_documento")+1;
						else
							ccc_numero_documento=1;
					}
					else
					{
						ccc_numero_documento=1;
					}
					
					smn_tasa=0;
					ccc_estatus="CO";
					ccc_idioma="es";
					ccc_usuario="admin";
					
					ResultSet moduloComercial=control.moduloComercial();
					
					if(getRecordCount(moduloComercial)>0)
					{
						moduloComercial.next();
						smn_modulo_rf=moduloComercial.getInt("smn_modulos_id");
					}
					else
					{
						str = "*No se encontro el modulo SMN_CME=Comercial*";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					str = "<<Registrando cierre contable...";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet RegistrarControlCierre=control.insertControl_cierre(smn_entidad_rf, smn_sucursal_rf, smn_documentos_generales_rf, smn_documento_id, fca_fecha_registro, ccc_numero_documento, fca_monto_factura_ml, fca_monto_factura_ma, smn_moneda_rf, smn_tasa, ccc_estatus, ccc_idioma, ccc_usuario, sqlDate, hora, smn_modulo_rf,smn_clase_auxiliar_rf,smn_auxiliar_rf);
					RegistrarControlCierre.next();
					
					smn_control_cierre_contable_id=RegistrarControlCierre.getInt("smn_control_cierre_contable_id");
					
					str = ">>Cierre contable registrado";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					str = "<<Actualizando facturas con el cierre contable registrado...";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					control.updateFacturaCabecera(smn_entidad_rf, smn_sucursal_rf, smn_documento_id, smn_moneda_rf, fca_fecha_registro, smn_control_cierre_contable_id);
					
					str = ">>Facturas actualizadas ";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet tipoDocumento=control.tipoDocumento(smn_modulo_rf, smn_documentos_generales_rf);
					
					if(getRecordCount(tipoDocumento)>0)
					{
						tipoDocumento.next();
						smn_tipo_documento_id=tipoDocumento.getInt("smn_tipo_documento_id");
						smn_rel_modulo_documento_tipo_doc_id=tipoDocumento.getInt("smn_rel_modulo_documento_tipo_doc_id");
					}
					else
					{
						str = "*No se encontro el tipo de documento contable*";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					ResultSet numeroDoc=control.numeroDocumento();
					
					if(getRecordCount(numeroDoc)>0)
					{
						numeroDoc.next();
						if(numeroDoc.getString("doc_numero_documento")!=null)
							doc_numero_documento=numeroDoc.getInt("doc_numero_documento")+1;
						else
							doc_numero_documento=1;
					}
					else
					{
						doc_numero_documento=1;
					}
					
					smn_clase_auxiliar_ord_rf=0;
					smn_auxiliar_ord_rf=0;
					doc_orden_compra_rf=0;
					smn_centro_costo_rf=0;
					smn_proyecto_rf=0;
					//doc_fecha_doc=sqlDate;
					doc_fecha_rec=sqlDate;
					doc_fecha_vcto=null;
					doc_planilla_importacion=null;
					smn_documentos_generales_rf_afecta=0;
					doc_numero_doc_afecta=0;
					doc_numero_control_doc_afect=0;
					doc_fecha_doc_afecta=null;
					smn_codigos_impuestos_rf=0;
					doc_numero_control_fiscal_inicial=0;
					doc_numero_control_fiscal_ultimo=0;
					doc_numero_control1_inicial=0;
					doc_numero_control1_ultimo=0;
					doc_numero_control2_inicial=0;
					doc_numero_control2_ultimo=0;
					doc_estatus="R";
					doc_ano_comprobante=0;
					doc_periodos_detalles_rf=0;

					ResultSet tipoComprobante=control.tipoComprobante(smn_rel_modulo_documento_tipo_doc_id);
					
					if(getRecordCount(tipoComprobante)>0)
					{
						tipoComprobante.next();
						smn_tipo_comprobante_id=tipoComprobante.getInt("mcc_tipo_comp");
					}
					else
					{
						str = "*No se encontro el tipo de comprobante contable*";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					doc_num_comprobante=0;
					doc_fecha_comprobante=null;
					doc_numero_control=null;
					smn_elemento_rf=0;
					//doc_descripcion=null;
					//smn_moneda_rf=0;
					
					if (fca_monto_factura_ml != 0 && fca_monto_factura_ma != 0 ){
						doc_tasa_cambio=fca_monto_factura_ml/fca_monto_factura_ma;
					}
					else {
						doc_tasa_cambio = 0;
					}
					
					//doc_tasa_cambio=fca_monto_factura_ml/fca_monto_factura_ma;
					
					str = "<<Registrando documento contable...";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet insertDoc=control.insertDocumento(smn_modulo_rf, smn_entidad_rf, smn_sucursal_rf, 
							smn_documentos_generales_rf, smn_tipo_documento_id, doc_numero_documento, 
							smn_clase_auxiliar_rf, smn_auxiliar_rf, smn_clase_auxiliar_ord_rf, 
							smn_auxiliar_ord_rf, doc_orden_compra_rf, smn_centro_costo_rf, smn_proyecto_rf, 
							fca_fecha_registro, doc_fecha_rec, doc_fecha_vcto, doc_planilla_importacion, 
							fca_monto_factura_ml, fca_monto_factura_ma, doc_tasa_cambio, smn_documentos_generales_rf_afecta, 
							doc_numero_doc_afecta, doc_numero_control_doc_afect, doc_fecha_doc_afecta, 
							smn_codigos_impuestos_rf, doc_numero_control_fiscal_inicial, doc_numero_control_fiscal_ultimo, 
							doc_numero_control1_inicial, doc_numero_control1_ultimo, doc_numero_control2_inicial, 
							doc_numero_control2_ultimo, doc_estatus, doc_ano_comprobante, doc_periodos_detalles_rf, 
							smn_tipo_comprobante_id, doc_num_comprobante, "es", "admin", sqlDate, hora, doc_fecha_comprobante,
							doc_numero_control, smn_elemento_rf, dcf_descripcion, 0);
					
					str = ">>Documento contable registrado";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					insertDoc.next();
					smn_documento_id_cont=insertDoc.getInt("smn_documento_id_cont");
					
					str = "Datos para la consultarDetalles" + "Entidad: " + smn_entidad_rf + " Sucursal: " + smn_sucursal_rf + " Documento_id; " +  smn_documento_id + " Moneda_rf: " +  smn_moneda_rf + " Fecha_registro: " + fca_fecha_registro;	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet detalles=control.consultarDetalles(smn_entidad_rf, smn_sucursal_rf, smn_documento_id, smn_moneda_rf, fca_fecha_registro);
					
					if(getRecordCount(detalles)>0)
					{
						while(detalles.next())
						{
							pco_tipo_componente=detalles.getString("pco_tipo_componente");
							smn_componente_rf=detalles.getInt("smn_componente_rf");
							
							if(detalles.getString("centro_costo")!=null)
								smn_centro_costo_rf=detalles.getInt("centro_costo");
							else
							{
								str = "*No se encontro el centro de costo*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							if(detalles.getString("smn_clase_auxiliar_rf")!=null)
								smn_clase_auxiliar_rf=detalles.getInt("smn_clase_auxiliar_rf");
							else
							{
								/*str = "*No se encontro la clase auxiliar*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;*/
								smn_clase_auxiliar_rf=0;
							}
							
							if(detalles.getString("smn_auxiliar_rf")!=null)
								smn_auxiliar_rf=detalles.getInt("smn_auxiliar_rf");
							else
							{
								/*str = "*No se encontro el auxiliar*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;*/
								smn_auxiliar_rf=0;
							}
							
							total_monto_ml=detalles.getDouble("total_monto_ml");
							
							if(detalles.getString("total_monto_ma")!=null)
								total_monto_ma=detalles.getDouble("total_monto_ma");
							else
								total_monto_ma=0;
								
							ResultSet diccionario=control.consultarDiccionario(pco_tipo_componente);
							
							if(getRecordCount(diccionario)>0)
							{
								diccionario.next();
								smn_diccionario_id=diccionario.getInt("smn_diccionario_id");
							}
							else
							{
								str = "*No se encontro el diccionario*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							ResultSet tipoElemento=control.consultarTipoElemento(smn_diccionario_id, smn_modulo_rf);
							
							if(getRecordCount(tipoElemento)>0)
							{
								tipoElemento.next();
								smn_tipo_elemento_id=tipoElemento.getInt("smn_tipo_elemento_id");
							}
							else
							{
								str = "*No se encontro el tipo de elemento*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							smn_tasa_rf=0;
							
							ResultSet componente=control.consultarComponente(smn_componente_rf);
							
							if(getRecordCount(componente)>0)
							{
								componente.next();
								cmp_descripcion=componente.getString("cmp_descripcion");
							}
							else
							{
								str = "*No se encontro el componente*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							dod_monto_neto_ml=total_monto_ml;
							dod_monto_neto_ma=total_monto_ma;
							dod_estatus="RE";
							
							str = "<<Registrando documento detalle...";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							
							control.insertDocumentoDetalle(smn_documento_id_cont, smn_tipo_elemento_id, 
									smn_componente_rf, smn_clase_auxiliar_rf, smn_auxiliar_rf, 
									smn_proyecto_rf, smn_centro_costo_rf, total_monto_ml, smn_moneda_rf, 
									smn_tasa_rf, total_monto_ma, dod_estatus, "es", "admin", sqlDate, 
									hora, dod_monto_neto_ml, dod_monto_neto_ma, cmp_descripcion);
							
							str = ">>Documento detalle registrado";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							
						} //end while detalles
					}
					else
					{
						str = "*No se encontraron detalles para procesar*";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					if(result==0)
					{
						str = "<<Actualizando estatus de las facturas...";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						
						control.updateFactura("CO", smn_entidad_rf, smn_sucursal_rf, smn_documento_id, smn_moneda_rf, fca_fecha_registro);
						
						str = ">>Estatus de las facturas actualizados";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						
						ResultSet facturas_contabilizadas=control.facturas_contabilizadas(smn_entidad_rf, smn_sucursal_rf, smn_documento_id, smn_moneda_rf, fca_fecha_registro);
						
						if(getRecordCount(facturas_contabilizadas)>0)
						{
							while(facturas_contabilizadas.next())
							{
								registrarOrdenPago(bw,control.cn,facturas_contabilizadas.getInt("smn_factura_cabecera_id"));
								registrarImpuestos(bw,control.cn,facturas_contabilizadas.getInt("smn_factura_cabecera_id"));
							}//end while facturas_contabilizadas
						}
						else
						{
							str = "*No se encontraron las facturas contabilizadas*";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							result=1;
							break;
						}
					}
					else
					{
						break;
					}
					
				}//end while facturas
			}
			else
			{
				str = "*No se encontraron facturas para procesar*";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				result=1;
			}
		}
		catch(Throwable e)
		{
			control.cn.rollback();
			throw e;
		}
		finally
		{
			if(result == 0)
			{
				control.cn.commit();
				str = "Cambios efectuados en la base de datos";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			}
			else
			{
				control.cn.rollback();
				str = "Cambios no efectuados en la base de datos";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			}
			
			if(control.cn!=null)
				control.cn.close();
			
			str = "FINAL DEL PROCESO";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			bw.close();
		}
	}
	
	public static int getRecordCount(ResultSet rs)
	{
		int total=0;
		
		try {
			boolean ultimo = rs.last();
			
			if (ultimo)
			{
		        total = rs.getRow();
		        rs.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return total;
	}
	
	public static int registrarOrdenPago(BufferedWriter bw, Connection db, int smn_factura_cabecera_id) throws Throwable
	{
		String str;
		String sql;
		int rc=0;
		int smn_orden_pago_id=0;
		int smn_documento_id;
		double opa_monto_factura_ml;
		double opa_monto_factura_ma;
		
		str = "<<Ejecutando proceso de registrar orden de pago...";	
		bw.write(str);
		bw.flush();
		bw.newLine();
		
		try
		{
			str = "<<Registrando orden de pago...";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			sql="insert into smn_pagos.smn_orden_pago ( "
			  + "smn_orden_pago_id, "
			  + "smn_documento_contable_rf, "
			  + "smn_entidades_rf, "
			  + "smn_sucursales_rf, "
			  + "smn_clase_auxiliar_rf, "
			  + "smn_auxiliar_rf, "
			  + "smn_usuario_rf, "
			  + "smn_tipo_documento_id, "
			  + "smn_documento_id, "
			  + "opa_numero, "
			  + "smn_modulo_rf, "
			  + "smn_documento_rf, "
			  + "opa_numero_documento, "
			  + "opa_descripcion, "
			  + "smn_centro_costo_rf, "
			  + "smn_proyecto_rf, "
			  + "smn_orden_compra_rf, "
			  + "opa_monto_factura_ml, "
			  + "opa_monto_factura_ma, "
			  + "opa_monto_pagado_ml, "
			  + "opa_monto_pagado_ma, "
			  + "opa_monto_dif_camb, "
			  + "opa_monto_dif_camb_pag, "
			  + "opa_saldo_factura_ml, "
			  + "opa_saldo_factura_ma, "
			  + "opa_saldo_opa_dif_cambiario, "
			  + "smn_moneda_rf, "
			  + "smn_tasa_cambio_rf, "
			  + "opa_estatus_financiero, "
			  + "opa_fecha_reverso, "
			  + "opa_estatus, "
			  + "opa_idioma, "
			  + "opa_usuario, "
			  + "opa_fecha_registro, "
			  + "tdo_hora, "
			  + "smn_clave_imp_ret_id, "
			  + "smn_orden_compra_op_id, "
			  + "opa_documento_descripcion_origen, "
			  + "opa_fecha_vencimiento, "
			  + "opa_fecha_recepcion, "
			  + "opa_monto_descuentos_ml, "
			  + "opa_monto_descuentos_ma, "
			  + "opa_monto_impuestos_ml, "
			  + "opa_monto_impuestos_ma, "
			  + "smn_ingresos_rf, "
			  + "smn_prestador_servicio_rf, "
			  + "smn_componente_rf) "
			  + "select nextval('smn_pagos.seq_smn_orden_pago'), "
			  + "rpf.smn_factura_cabecera_id, "
			  + "pca.smn_entidad_rf, "
			  + "(case when pca.smn_sucursal_rf is null then 0 else pca.smn_sucursal_rf end), "
			  + "pco.smn_clase_auxiliar_rf, "
			  + "pco.smn_auxiliar_rf, "
			  + "case when (select smn_usuarios_id from smn_base.smn_usuarios where smn_clase_auxiliar_rf=pco.smn_clase_auxiliar_rf and smn_auxiliar_rf=pco.smn_auxiliar_rf) is null then 0 else (select smn_usuarios_id from smn_base.smn_usuarios where smn_clase_auxiliar_rf=pco.smn_clase_auxiliar_rf and smn_auxiliar_rf=pco.smn_auxiliar_rf) end, "
			  + "(select smn_tipo_documento_id from smn_pagos.smn_tipo_documento where tdo_codigo='OP'), "
			  + "(select smn_pagos.smn_documento.smn_documento_id from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id), "
			  + "(select smn_pagos.smn_documento.doc_secuencia+1 from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id), "
			  + "(select smn_modulos_id from smn_base.smn_modulos where mod_codigo='SMN_CME'), "
			  + "pca.smn_documento_id, "
			  + "pca.pca_numero_pedido, "
			  + "pca.pca_descripcion, "
			  + "Null, "
			  + "Null, "
			  + "Null, "
			  + "pco.pco_monto_ml, "
			  + "pco.smn_monto_ma, "
			  + "0, "
			  + "0, "
			  + "0, "
			  + "0, "
			  + "pco.pco_monto_ml, "
			  + "pco.smn_monto_ma, "
			  + "0, "
			  + "pco.smn_moneda_rf, "
			  + "pco.smn_tasa_rf, "
			  + "'GE', "
			  + "Null, "
			  + "'GE', "
			  + "'es', "
			  + "'admin', "
			  + "CURRENT_DATE, "
			  + "(select to_char(current_timestamp, 'HH24:MI:SS')), "
			  + "Null, "
			  + "Null, "
			  + "'HONORARIOS MEDICOS', "
			  + "pca.pca_fecha_vencimiento, "
			  + "pca.pca_fecha_registro, "
			  + "pco.pco_monto_ml * (select sum((dyr_porcentaje_base/100)*(dyr_porcentaje_descuento/100)) "
			  + "from smn_pagos.smn_documento doc "
			  + "inner join smn_pagos.smn_rel_documento_descuentos rdd on rdd.smn_documento_id=doc.smn_documento_id "
			  + "inner join smn_base.smn_descuentos_retenciones dre on dre.smn_descuentos_retenciones_id=rdd.smn_descuentos_retenciones_rf "
			  + "where doc.smn_documento_id=(select smn_pagos.smn_documento.smn_documento_id from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id)), "
			  + "pco.smn_monto_ma * (select sum((dyr_porcentaje_base/100)*(dyr_porcentaje_descuento/100)) "
			  + "from smn_pagos.smn_documento doc "
			  + "inner join smn_pagos.smn_rel_documento_descuentos rdd on rdd.smn_documento_id=doc.smn_documento_id "
			  + "inner join smn_base.smn_descuentos_retenciones dre on dre.smn_descuentos_retenciones_id=rdd.smn_descuentos_retenciones_rf "
			  + "where doc.smn_documento_id=(select smn_pagos.smn_documento.smn_documento_id from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id)), "
			  + "pco.pco_monto_ml * (select sum((imp_porcentaje_base/100)*(imp_porcentaje_calculo/100)) "
			  + "from smn_pagos.smn_documento doc "
			  + "inner join smn_pagos.smn_rel_documento_impuesto rdi on rdi.smn_documento_id=doc.smn_documento_id "
			  + "inner join smn_base.smn_codigos_impuestos cdi on cdi.smn_codigos_impuestos_id=rdi.smn_codigos_impuestos_rf "
			  + "where doc.smn_documento_id=(select smn_pagos.smn_documento.smn_documento_id from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id)), "
			  + "pco.smn_monto_ma *(select sum((imp_porcentaje_base/100)*(imp_porcentaje_calculo/100)) "
			  + "from smn_pagos.smn_documento doc "
			  + "inner join smn_pagos.smn_rel_documento_impuesto rdi on rdi.smn_documento_id=doc.smn_documento_id "
			  + "inner join smn_base.smn_codigos_impuestos cdi on cdi.smn_codigos_impuestos_id=rdi.smn_codigos_impuestos_rf "
			  + "where doc.smn_documento_id=(select smn_pagos.smn_documento.smn_documento_id from smn_pagos.smn_documento "
			  + "inner join smn_base.smn_documentos_generales on  smn_base.smn_documentos_generales.smn_documentos_generales_id=smn_pagos.smn_documento.smn_documentos_generales_rf "
			  + "inner join smn_comercial.smn_documento on smn_comercial.smn_documento.smn_documentos_generales_rf=smn_base.smn_documentos_generales.smn_documentos_generales_id "
			  + "where smn_comercial.smn_documento.smn_documento_id=pca.smn_documento_id)), "
			  + "pco.smn_ingresos_rf, "
			  + "pco.smn_prestador_servicio_rf, "
			  + "pco.smn_componente_rf "
			  + "from smn_comercial.smn_rel_pedido_factura rpf "
			  + "inner join smn_comercial.smn_pedido_cabecera pca on pca.smn_pedido_cabecera_id=rpf.smn_pedido_cabecera_id "
			  + "inner join smn_comercial.smn_pedido_detalle pdt on pdt.smn_pedido_cabecera_id=pca.smn_pedido_cabecera_id "
			  + "inner join smn_comercial.smn_pedido_componentes pco on pco.smn_pedido_detalle_id=pdt.smn_pedido_detalle_id "
			  + "where rpf.smn_factura_cabecera_id=? "
			  + "and pco_tipo_componente='HO' "
			  + "RETURNING smn_orden_pago_id, opa_monto_factura_ml, opa_monto_factura_ma, smn_documento_id";
			
			PreparedStatement preparedStmt1 = db.prepareStatement(sql);
			preparedStmt1.setInt(1, smn_factura_cabecera_id);
			ResultSet rs=preparedStmt1.executeQuery();
			
			while(rs.next())
			{
				smn_orden_pago_id=rs.getInt("smn_orden_pago_id");
				opa_monto_factura_ml=rs.getDouble("opa_monto_factura_ml");
				opa_monto_factura_ma=rs.getDouble("opa_monto_factura_ma");
				smn_documento_id=rs.getInt("smn_documento_id");
				
				if(smn_orden_pago_id==0)
				{
					str = "*No se registro la orden de pago*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				str = "<<orden de pago registrada";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				str = "<<Registrando relacion orden de pago nrs...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				sql="Insert into smn_pagos.smn_rel_orden_pago_nrs ( "
						  + "smn_rel_orden_pago_nrs_id, "
						  + "smn_nota_recepcion_servicio_id, "
						  + "smn_orden_pago_id "
						  + ") "
						  + "(select  nextval('smn_pagos.seq_smn_rel_orden_pago_nrs'), "
						  + "(select smn_nota_recepcion_servicio_id from smn_comercial.smn_rel_pedido_factura rpf "
						  + "inner join smn_comercial.smn_pedido_cabecera pca on pca.smn_pedido_cabecera_id=rpf.smn_pedido_cabecera_id "
						  + "inner join smn_comercial.smn_pedido_detalle pdt on pdt.smn_pedido_cabecera_id=pca.smn_pedido_cabecera_id "
						  + "inner join smn_comercial.smn_pedido_componentes pco on pco.smn_pedido_detalle_id=pdt.smn_pedido_detalle_id "
						  + "inner join smn_pagos.smn_nota_recepcion_servicio nrs on nrs.smn_ingresos_rf=pco.smn_ingresos_rf and "
						  + "nrs.smn_prestador_servicio_rf=pco.smn_prestador_servicio_rf and nrs.smn_componente_rf=pco.smn_componente_rf "
						  + "where rpf.smn_factura_cabecera_id=? "
						  + "and pco_tipo_componente='HO'), "
						  + "?)";
				
				PreparedStatement preparedStmt = db.prepareStatement(sql);
				preparedStmt.setInt(1, smn_factura_cabecera_id);
				preparedStmt.setInt(2, smn_orden_pago_id);
				preparedStmt.execute();
				
				str = "<<Relacion orden de pago nrs registrada";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				str = "<<Registrando relacion orden de pago descuentos...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				sql="Insert into smn_pagos.smn_descuento_retencion ( "
						  + "smn_descuento_retencion_op_id, "
						  + "smn_orden_pago_id, "
						  + "smn_descuento_retencion_rf, "
						  + "dro_monto_base_ml, "
						  + "dro_monto_base_ma, "
						  + "dro_porcentaje, "
						  + "dro_monto_descuento_ml, "
						  + "dro_monto_descuento_ma, "
						  + "dro_idioma, "
						  + "dro_usuario, "
						  + "dro_fecha_registro, "
						  + "dro_hora "
						  + ") "
						  + "Select "
						  + "nextval('smn_pagos.seq_smn_descuento_retencion'), "
						  + "?, "
						  + "rdd.smn_descuentos_retenciones_rf, "
						  + "? * dre.dyr_porcentaje_base/100, "
						  + "? * dre.dyr_porcentaje_base/100, "
						  + "dre.dyr_porcentaje_descuento, "
						  + "? * (dre.dyr_porcentaje_base/100 * dre.dyr_porcentaje_descuento/100), "
						  + "? * (dre.dyr_porcentaje_base/100 * dre.dyr_porcentaje_descuento/100), "
						  + "'es', "
						  + "'admin', "
						  + "CURRENT_DATE, "
						  + "(select to_char(current_timestamp, 'HH24:MI:SS')) "
						  + "from smn_pagos.smn_documento doc "
						  + "inner join smn_pagos.smn_rel_documento_descuentos rdd on rdd.smn_documento_id=doc.smn_documento_id "
						  + "inner join smn_base.smn_descuentos_retenciones dre on dre.smn_descuentos_retenciones_id=rdd.smn_descuentos_retenciones_rf "
						  + "where doc.smn_documento_id=?"; 
				
				PreparedStatement preparedStmt2 = db.prepareStatement(sql);
				preparedStmt2.setInt(1, smn_orden_pago_id);
				preparedStmt2.setDouble(2, opa_monto_factura_ml);
				preparedStmt2.setDouble(3, opa_monto_factura_ma);
				preparedStmt2.setDouble(4, opa_monto_factura_ml);
				preparedStmt2.setDouble(5, opa_monto_factura_ma);
				preparedStmt2.setInt(6, smn_documento_id);
				preparedStmt2.execute();
				
				str = "<<Relacion orden de pago descuentos registrada";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				str = "<<Registrando relacion orden de pago impuestos...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				sql="Insert into smn_pagos.smn_impuestos_retenciones ( "
				   + "smn_impuestos_retenciones_id, "
				   + "smn_orden_pago_id, "
				   + "smn_codigo_impuesto_rf, "
				   + "iop_base_imponible_ml, "
				   + "iop_base_imponible_ma, "
				   + "iop_base_excenta_ml, "
				   + "iop_base_excenta_ma, "
				   + "iop_porcentaje_retencion, "
				   + "iop_monto_impuesto_ml, "
				   + "iop_monto_impuesto_ma, "
				   + "iop_idioma, "
				   + "iop_usuario, "
				   + "iop_fecha_registro, "
				   + "iop_hora "
				   + ") "
				   + "Select "
				   + "nextval('smn_pagos.seq_smn_impuestos_retenciones'), "
				   + "?, "
				   + "rdi.smn_codigos_impuestos_rf, "
				   + "? * cdi.imp_porcentaje_base/100, "
				   + "? * cdi.imp_porcentaje_base/100, "
				   + "0, "
				   + "0, "
				   + "cdi.imp_porcentaje_calculo, "
				   + "? * (cdi.imp_porcentaje_base/100 * cdi.imp_porcentaje_calculo/100), "
				   + "? * (cdi.imp_porcentaje_base/100 * cdi.imp_porcentaje_calculo/100), "
				   + "'es', "
				   + "'admin', "
				   + "CURRENT_DATE, "
				   + "(select to_char(current_timestamp, 'HH24:MI:SS')) "
				   + "from smn_pagos.smn_documento doc "
				   + "inner join smn_pagos.smn_rel_documento_impuesto rdi on rdi.smn_documento_id=doc.smn_documento_id "
				   + "inner join smn_base.smn_codigos_impuestos cdi on cdi.smn_codigos_impuestos_id=rdi.smn_codigos_impuestos_rf "
				   + "where doc.smn_documento_id=?";  
				
				PreparedStatement preparedStmt3 = db.prepareStatement(sql);
				preparedStmt3.setInt(1, smn_orden_pago_id);
				preparedStmt3.setDouble(2, opa_monto_factura_ml);
				preparedStmt3.setDouble(3, opa_monto_factura_ma);
				preparedStmt3.setDouble(4, opa_monto_factura_ml);
				preparedStmt3.setDouble(5, opa_monto_factura_ma);
				preparedStmt3.setInt(6, smn_documento_id);
				preparedStmt3.execute();
				
				str = "<<Relacion orden de pago impuestos registrada";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			}
		}
		catch(Throwable e)
		{
			rc=1;
			throw e;
		}
		
		return rc;
	}
	
	public static int registrarImpuestos(BufferedWriter bw, Connection db, int smn_factura_cabecera_id) throws Throwable
	{
		ControlDB control=new ControlDB();
		String str;
		int rc=0;
		
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual= sdformat.format(new Date());
		
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = date.parse(fechaActual);
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        
        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        int seconds = locaDate.getSecond();
        String hora = hours+":"+minutes+":"+seconds;
		
		int smn_entidad_rf;
		int smn_sucursal_rf;
		int smn_documento_id;
		int smn_cliente_id;
		String fca_rif;
		int fca_numero_documento;
		int fca_numero_control_fiscal;
		java.sql.Date fca_fecha_registro;
		double fim_monto_ml=0;
		double fim_monto_ma;
		int smn_codigos_impuestos_rf;
		int imp_porcentaje_calculo;
		double fim_monto_impuesto_ml;
		double imp_monto_sustraendo;
		int smn_moneda_rf;
		int smn_tasa_rf;
		double fim_monto_impuesto_ma;
		int smn_modulo_rf;
		int smn_auxiliar_rf;
		int smn_clase_auxiliar_rf;
		double imp_porcentaje_base;
		String gti_venta_terceros;
		double imp_monto_neto_impuesto;
		double imp_monto_neto_impuesto_ma;
		
		str = "<<Ejecutando proceso de registrar impuestos...";	
		bw.write(str);
		bw.flush();
		bw.newLine();
		
		try
		{
			str = "<<Consultando datos de la factura...";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			ResultSet factura=control.consultarFactura(smn_factura_cabecera_id);
			
			while(factura.next())
			{
				smn_entidad_rf=factura.getInt("smn_entidad_rf");
				smn_documento_id=factura.getInt("smn_documento_id");
				fca_rif=factura.getString("fca_rif");
				fca_numero_documento=factura.getInt("fca_numero_documento");
				fca_fecha_registro=factura.getDate("fca_fecha_registro");
				
				if(factura.getString("smn_codigos_impuestos_rf")!=null)
					smn_codigos_impuestos_rf=factura.getInt("smn_codigos_impuestos_rf");
				else
				{
					str = "*El pedido no tiene codigo de impuesto asociado*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				if(factura.getString("smn_cliente_id")!=null)
					smn_cliente_id=factura.getInt("smn_cliente_id");
				else
				{
					str = "*La factura no tiene cliente asociado*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				if(factura.getString("gti_venta_terceros")!=null)
					gti_venta_terceros=factura.getString("gti_venta_terceros");
				else
				{
					str = "*La factura no tiene grupo titulo impresion*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				if(factura.getString("smn_sucursal_rf")!=null)
					smn_sucursal_rf=factura.getInt("smn_sucursal_rf");
				else
					smn_sucursal_rf=0;
				
				if(factura.getString("fca_numero_documento")!=null)
					fca_numero_control_fiscal=factura.getInt("fca_numero_documento");
				else
					fca_numero_control_fiscal=0;
				
				if(factura.getString("imp_porcentaje_calculo")!=null)
					imp_porcentaje_calculo=factura.getInt("imp_porcentaje_calculo");
				else
					imp_porcentaje_calculo=0;
				
				if(factura.getString("imp_monto_sustraendo")!=null)
					imp_monto_sustraendo=factura.getDouble("imp_monto_sustraendo");
				else
					imp_monto_sustraendo=0;
				
				if(factura.getString("fim_monto_impuesto_ml")!=null)
					fim_monto_impuesto_ml=factura.getDouble("fim_monto_impuesto_ml");
				else
					fim_monto_impuesto_ml=0;
				
				if(factura.getString("smn_moneda_rf")!=null)
					smn_moneda_rf=factura.getInt("smn_moneda_rf");
				else
					smn_moneda_rf=0;
				
				if(factura.getString("smn_tasa_rf")!=null)
					smn_tasa_rf=factura.getInt("smn_tasa_rf");
				else
					smn_tasa_rf=0;
				
				if(factura.getString("fim_monto_impuesto_ma")!=null)
					fim_monto_impuesto_ma=factura.getDouble("fim_monto_impuesto_ma");
				else
					fim_monto_impuesto_ma=0;
				
				if(factura.getString("fim_monto_ma")!=null)
					fim_monto_ma=factura.getDouble("fim_monto_ma");
				else
					fim_monto_ma=0;
				
				str = "<<Consultando modulo de origen CME...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				ResultSet moduloComercial=control.moduloComercial();
				
				if(getRecordCount(moduloComercial)>0)
				{
					moduloComercial.next();
					smn_modulo_rf=moduloComercial.getInt("smn_modulos_id");
				}
				else
				{
					str = "*No se encontro el modulo SMN_CME=Comercial*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				str = "<<Consultando auxiliar y clase auxiliar del cliente...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				ResultSet cliente=control.consultarAuxiliarCliente(smn_cliente_id);
				
				if(getRecordCount(cliente)>0)
				{
					cliente.next();
					smn_auxiliar_rf=cliente.getInt("smn_auxiliar_rf");
					smn_clase_auxiliar_rf=cliente.getInt("smn_clase_auxiliar_rf");
				}
				else
				{
					str = "*No se encontro el cliente*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				str = "<<Consultando codigo de impuesto...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				ResultSet codigo_impuesto=control.consultarCodigoImpuesto(smn_codigos_impuestos_rf);
				
				if(getRecordCount(codigo_impuesto)>0)
				{
					codigo_impuesto.next();
					imp_porcentaje_base=codigo_impuesto.getDouble("imp_porcentaje_base");
				}
				else
				{
					str = "*No se encontro el codigo de impuesto asociado al pedido*";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					return 1;
				}
				
				str = "<<Calculando montos netos...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				imp_monto_neto_impuesto=fim_monto_impuesto_ml-imp_monto_sustraendo;
				imp_monto_neto_impuesto_ma=fim_monto_impuesto_ma-imp_monto_sustraendo;
				
				str = "<<Montos netos calculados";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				str = "<<Registrando impuesto en contabilidad financiera...";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				
				control.insertImpuesto(smn_entidad_rf, smn_sucursal_rf, smn_documento_id, smn_modulo_rf, smn_clase_auxiliar_rf, smn_auxiliar_rf, fca_rif, fca_numero_documento, fca_numero_control_fiscal, fca_fecha_registro, imp_porcentaje_base, fim_monto_ml, gti_venta_terceros, smn_codigos_impuestos_rf, imp_porcentaje_calculo, fim_monto_impuesto_ml, imp_monto_sustraendo, imp_monto_neto_impuesto, "es", "admin", sqlDate, 
						hora, smn_moneda_rf, smn_tasa_rf, fim_monto_impuesto_ma, fim_monto_ma, imp_monto_neto_impuesto_ma);
				
				str = "<<Impuesto registrado";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			} //end rs
		}
		catch(Throwable e)
		{
			rc=1;
			throw e;
		}
		
		return rc;
	}
}
