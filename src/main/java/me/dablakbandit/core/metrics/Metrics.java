package me.dablakbandit.core.metrics;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * bStats collects some data for plugin authors.
 *
 * Check out https://bStats.org/ to learn more about bStats!
 */
public class Metrics{


	private final NewMetrics metrics;

	// The plugin
	private final JavaPlugin		plugin;
	
	private final String			pluginName;
	
	/**
	 * Class constructor.
	 *
	 * @param plugin The plugin which stats should be submitted.
	 */
	public Metrics(JavaPlugin plugin){
		this(plugin, plugin.getDescription().getName());
	}
	
	public Metrics(JavaPlugin plugin, String name){
		if(plugin == null){ throw new IllegalArgumentException("Plugin cannot be null!"); }
		this.plugin = plugin;
		this.pluginName = name;
		this.metrics = new NewMetrics(plugin, getServiceId(name));
	}

	private int getServiceId(String name){
		switch (name){
			case"Core_": return 2565;
			case "Bank": return 1417;
			case "Editor": return 2343;
			case "Mail": return 2344;
			case "BankLite": return 3267;
			case "QueryMe": return 4158;
		}
		return -1;
	}

	public void addCustomChart(CustomChart chart){
		if(chart == null){ throw new IllegalArgumentException("Chart cannot be null!"); }
		if(chart instanceof SimplePie){
			metrics.addCustomChart(new NewMetrics.SimplePie(chart.chartId, ((SimplePie) chart).callable));
		}else if(chart instanceof AdvancedPie){
			metrics.addCustomChart(new NewMetrics.AdvancedPie(chart.chartId, ((AdvancedPie) chart).callable));
		}else if(chart instanceof DrilldownPie){
			metrics.addCustomChart(new NewMetrics.DrilldownPie(chart.chartId, ((DrilldownPie) chart).callable));
		}else if(chart instanceof SingleLineChart){
			metrics.addCustomChart(new NewMetrics.SingleLineChart(chart.chartId, ((SingleLineChart) chart).callable));
		}else if(chart instanceof MultiLineChart){
			metrics.addCustomChart(new NewMetrics.MultiLineChart(chart.chartId, ((MultiLineChart) chart).callable));
		}else if(chart instanceof SimpleBarChart){
			metrics.addCustomChart(new NewMetrics.SimpleBarChart(chart.chartId, ((SimpleBarChart) chart).callable));
		}else if(chart instanceof AdvancedBarChart){
			metrics.addCustomChart(new NewMetrics.AdvancedBarChart(chart.chartId, ((AdvancedBarChart) chart).callable));
		}
	}
	
	/**
	 * Represents a custom chart.
	 */
	public static abstract class CustomChart{
		
		// The id of the chart
		final String chartId;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 */
		CustomChart(String chartId) {
			if (chartId == null || chartId.isEmpty()) {
				throw new IllegalArgumentException("ChartId cannot be null or empty!");
			}
			this.chartId = chartId;
		}
		
	}
	
	/**
	 * Represents a custom simple pie.
	 */
	public static class SimplePie extends CustomChart{
		
		private final Callable<String> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public SimplePie(String chartId, Callable<String> callable){
			super(chartId);
			this.callable = callable;
		}

	}
	
	/**
	 * Represents a custom advanced pie.
	 */
	public static class AdvancedPie extends CustomChart{
		
		private final Callable<Map<String, Integer>> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable){
			super(chartId);
			this.callable = callable;
		}
	}
	
	/**
	 * Represents a custom drilldown pie.
	 */
	public static class DrilldownPie extends CustomChart {

		private final Callable<Map<String, Map<String, Integer>>> callable;

		/**
		 * Class constructor.
		 *
		 * @param chartId  The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
			super(chartId);
			this.callable = callable;
		}

	}
	
	/**
	 * Represents a custom single line chart.
	 */
	public static class SingleLineChart extends CustomChart{
		
		private final Callable<Integer> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public SingleLineChart(String chartId, Callable<Integer> callable){
			super(chartId);
			this.callable = callable;
		}
		
	}
	
	/**
	 * Represents a custom multi line chart.
	 */
	public static class MultiLineChart extends CustomChart{
		
		private final Callable<Map<String, Integer>> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable){
			super(chartId);
			this.callable = callable;
		}
		
	}
	
	/**
	 * Represents a custom simple bar chart.
	 */
	public static class SimpleBarChart extends CustomChart{
		
		private final Callable<Map<String, Integer>> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable){
			super(chartId);
			this.callable = callable;
		}
		
	}
	
	/**
	 * Represents a custom advanced bar chart.
	 */
	public static class AdvancedBarChart extends CustomChart{
		
		private final Callable<Map<String, int[]>> callable;
		
		/**
		 * Class constructor.
		 *
		 * @param chartId The id of the chart.
		 * @param callable The callable which is used to request the chart data.
		 */
		public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable){
			super(chartId);
			this.callable = callable;
		}
		
	}
}
