package maqetta.server.orion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.orion.internal.server.servlets.ProtocolConstants;
import org.eclipse.orion.internal.server.servlets.workspace.WebProject;
import org.eclipse.orion.internal.server.servlets.workspace.WebWorkspace;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.maqetta.server.IStorage;
import org.maqetta.server.IVResource;
import org.maqetta.server.VDirectory;
import org.maqetta.server.VWorkspaceRoot;
import org.osgi.service.prefs.BackingStoreException;


public class VOrionWorkspace extends VWorkspaceRoot{

	private VOrionWorkspaceStorage store;
	
    public VOrionWorkspace(VOrionWorkspaceStorage storage) {
    	this.store=storage;
	}

	public IVResource create(String path) {
		IPath ps = new Path(path);
		IVResource parent = this;
		
		for(int i=0;i<ps.segmentCount();i++){
			String segment = ps.segment(i);
			IVResource f= parent.get(segment);
			if(f==null ){
				IStorage file = this.store.create(path);
				f = new VOrionResource(file, parent, segment);
				
				
			}else if(f.isVirtual()){
				
				IStorage file = this.store.create(path);
				if(f.isDirectory())
					file.mkdirs();
				f = new VOrionResource(file, parent, segment);
				parent.add(f);
				
			}
			parent = f;
		}
		
		
		return parent;
	}

}
