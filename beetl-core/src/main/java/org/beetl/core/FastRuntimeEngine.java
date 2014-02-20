package org.beetl.core;

import java.io.Reader;
import java.util.Map;

import org.beetl.core.probe.BasicProgramOptProbe;
import org.beetl.core.probe.Probe;
import org.beetl.core.probe.TypeBindingProbe;
import org.beetl.core.statement.Program;

public class FastRuntimeEngine extends DefaultTemplateEngine
{

	@Override
	public Program createProgram(String id, Reader reader, Map<Integer, String> textMap, String cr, GroupTemplate gt)
	{

		Program program = super.createProgram(id, reader, textMap, cr, gt);
		Probe nextFilter = new BasicProgramOptProbe(program);
		Probe filter = new TypeBindingProbe(program, nextFilter);
		FilterProgram filterProgram = new FilterProgram(program, filter);
		return filterProgram;
	}

	class FilterProgram extends Program
	{
		Probe filter = null;

		public FilterProgram(Program program, Probe filter)
		{
			this.metaData = program.metaData;
			this.filter = filter;

		}

		public void execute(Context ctx)
		{

			filter.check(ctx);
			super.execute(ctx);

		}
	}
}