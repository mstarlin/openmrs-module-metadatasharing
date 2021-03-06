/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.metadatasharing.web.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Class dedicated for DataTables. It is a response that can be translated to JSON.
 * 
 * @see http://datatables.net/
 */
public class DatatablesResponse {
	
	private Integer sEcho;
	
	private Integer iTotalRecords;
	
	private Integer iTotalDisplayRecords;
	
	private String sColumns;
	
	private transient String[] columns;
	
	private List<String[]> aaData = new ArrayList<String[]>();
	
	private DatatablesResponse(DatatablesRequest request) {
		sEcho = request.getsEcho();
	}
	
	/**
	 * An unaltered copy of sEcho sent from the client side. This parameter will change with each
	 * draw (it is basically a draw count) - so it is important that this is implemented. Note that
	 * it strongly recommended for security reasons that you 'cast' this parameter to an integer in
	 * order to prevent Cross Site Scripting (XSS) attacks.
	 * 
	 * @return the sEcho
	 */
	public Integer getsEcho() {
		return sEcho;
	}
	
	/**
	 * @see #setiTotalRecords(Integer)
	 * @return the iTotalRecords
	 */
	public Integer getiTotalRecords() {
		return iTotalRecords;
	}
	
	/**
	 * Total records, before filtering (i.e. the total number of records in the database).
	 * 
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(Integer iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	
	/**
	 * @see #setiTotalDisplayRecords(Integer)
	 * @return the iTotalDisplayRecords
	 */
	public Integer getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	
	/**
	 * Total records, after filtering (i.e. the total number of records after filtering has been
	 * applied - not just the number of records being returned in this result set).
	 * 
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	
	/**
	 * @see #setsColumns(String...)
	 * @return the sColumns
	 */
	public String[] getsColumns() {
		return columns;
	}
	
	/**
	 * Optional - this is a string of column names, comma separated (used in combination with sName)
	 * which will allow DataTables to reorder data on the client-side if required for display. Note
	 * that the number of column names returned must exactly match the number of columns in the
	 * table. For a more flexible JSON format, please consider using mDataProp.
	 * 
	 * @param sColumns the sColumns to set
	 */
	public void setsColumns(String... sColumns) {
		if (aaData != null && !aaData.isEmpty() && aaData.get(0).length != sColumns.length) {
			throw new IllegalArgumentException("Must contain " + aaData.get(0).length + " columns whereas "
			        + sColumns.length + " given");
		}
		this.columns = sColumns;
		this.sColumns = StringUtils.join(sColumns, ",");
	}
	
	/**
	 * The data in a 2D array. Note that you can change the name of this parameter with
	 * sAjaxDataProp.
	 * 
	 * @return the aaData
	 */
	public String[][] getAaData() {
		if (aaData != null) {
			return aaData.toArray(new String[aaData.size()][]);
		} else {
			return new String[0][];
		}
	}
	
	/**
	 * Adds the given row to the data. Consecutive calls must contain the same amount of columns.
	 * 
	 * @param row
	 */
	public void addRow(String... row) {
		if (sColumns != null && columns.length != row.length) {
			throw new IllegalArgumentException("Must contain " + columns.length
			        + " columns as declared in sColumns whereas " + row.length + " given");
		}
		
		if (aaData != null && !aaData.isEmpty() && aaData.get(0).length != row.length) {
			throw new IllegalArgumentException("Must contain " + aaData.get(0).length + " columns whereas " + row.length
			        + " given");
		}
		
		if (aaData == null) {
			aaData = new ArrayList<String[]>();
		}
		
		for (int i = 0; i < row.length; i++) {
			if (row[i] == null) {
				row[i] = "";
			}
		}
		
		aaData.add(row);
	}
	
	/**
	 * Adds the given row to the data. 
	 * 
	 * @param row
	 */
	public void addRow(Map<String, String> row) {
		if (sColumns == null) {
			setsColumns(row.keySet().toArray(new String[0]));
		}
		
		String[] rowed = new String[columns.length];
		for (int i = 0; i < rowed.length; i++) {
			String cell = row.get(columns[i]);
			rowed[i] = (cell != null) ? cell : "";
		}
		
		addRow(rowed);
	}
	
	public static DatatablesResponse newResponse(DatatablesRequest request) {
		return new DatatablesResponse(request);
	}
}
