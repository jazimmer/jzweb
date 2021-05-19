__doc__="""
Creates a workspace environment for the Python command line interpreter.

   1) Enables much of the current state to be saved and restored at a 
      later date.  Functions save() and load() are provided for this.
   
   2) Enables popup windows for editing individual functions and
      classes.  Function edit() is provided for this.
      
   3) Enables the current folder (directory) to be changed from a file
      chooser window. Function changeFolder() is provided for this.

   4) Provides functions readStr and writeStr for reading and writing
      entire text files.
      
A workspace such as this encourages you to think interpretively. By that
is meant you are encouraged to experiment with small pieces of small
projects.  This try-as-you-go approach is especially helpful for
beginners.   Experienced programmers working on large projects following
well trodden paths will have no use for this approach.

The workspace module requires Python version 2.4 or above and  
uses Tkinter.  If Tkinter is missing, functionality is restricted. 
Tkinter is automatically installed in the Windows version of Python.
(Windows is a MICROSOFT TRADMARK -- pause for a moment of reverence).

Written by J Adrian Zimmer, jazimmer.net, -- this is version 0.1.1
Released under version 2.1 of the Open Software License
"""

__all__ = []   # but see the end of this file

print "\nInstalling: ",

import traceback
import re
import cPickle
import sys
import os
import string
import __main__

try:
   import Tkinter as tk
except:
   tk = None

class _WorkspaceError (StandardError): pass

_Defs = {}
 # keys() will always be the names of functions or classes known to
 # this system (i.e. defined with define() *and* successfully compiled)
 
_Global = __main__.__dict__

_NO_NO = re.compile(r".\n[dc]",re.DOTALL).search

_IS_MAGIC = re.compile(r'^\_\_[^\_]+\_\_$').search

## _IS_OKMOD, _IS_FUNTYPE, and _IS_PRIMTYPE assume parameter is
## a string in _Global.keys(); they tell about the type of the
## corresponding variable in our main module

_IS_OKMOD = \
 lambda x: not _IS_MAGIC(x) and \
           type(_Global[x])==type(re) and \
           x<>'workspace' and \
           (eval(x,_Global)).__name__ == x
 ## major rewrite for version 0.9.1

_PRIMTYPE = \
    [  type(1), type(long(1)), type(float(1)), \
       type(complex(1)), type(''), type(False), type(None) ]

_IS_PRIMTYPE = \
 lambda x: not _IS_MAGIC(x) and type(_Global[x]) in _PRIMTYPE

_IDENT1 = r"([a-zA-Z0-9\_]+)"

_BEGIN2 = re.compile( _IDENT1 + "\s+" + _IDENT1 ).search 

_LIMIT = lambda x,y: filter( lambda z: z in y, x ) 

FUNTYPE = type(_IS_OKMOD)
CLASSTYPE = type(_WorkspaceError)
OBJTYPE = type(_WorkspaceError())

_ISTYPE = lambda name,typ: type(eval(name,_Global))==typ

_ALL_OK = "%s is OK"

_PLEASE_UPGRADE = "version 2.4, or later, of Python is required"

_OK_TUPLE = lambda x: type(x)==type(()) and \
                  len(x)==len(filter( lambda z: z in _PRIMITIVE, x ))

def _savableVariables():
    return filter( _savableV, _Global.keys() )

def _savableDefinitions():
    global _Defs, _Global
    deleteMe = filter( lambda x: not _Global.has_key(x), _Defs.keys() )
    for n in deleteMe:  del _Defs[n]
    return _Defs.keys()

def _savableModules():
    return filter( _IS_OKMOD, _Global.keys() )
    
def _savableV(x,isGlobalName=True):

   if isGlobalName:
       gx = eval(x, _Global)
       gt = type(gx)
   else:
       gx = x
       gt = type(x)
       
   if gt== type({}):
       for k in gx.keys():
           if type(k) in _PRIMTYPE or _SIMPLE_TUPLE(k): # restrictive
              v = gx[k]
              if type(v) in _PRIMTYPE: continue
              elif not _savableV(v,False): return False
           else:
              return False
       return True
   elif gt==type([]) or gt == type(()):
       for k in gx:
           if type(k) in _PRIMTYPE: continue
           if not _savableV(k,False):  return False
       return True
   elif isGlobalName:
       return _IS_PRIMTYPE(x)  # excludes magic names
   else:
       return gt in _PRIMTYPE

def _begins(name):
    retval = None
    sob = _BEGIN2(_Defs[name])
    if sob: 
        retval = (sob.group(1),sob.group(2))
        print "header found " + string.join(retval,' ')
    else:
        print "no header found"
    return retval

    
def _clearWorkspace():
    global _Defs, _Global
    for n in _savableVariables():
        exec  "del %s" % n  in  _Global, _Global
    for n in _savableDefinitions(): 
        exec  "del %s" % n  in  _Global, _Global

def changeFolder(folderName=None):
    """ changes the current folder (i.e. directory)
        
        If Tkinter is not installed, changeFolder(fileName) changes to directory named fileName and changeFolder() is pointless.

        If Tkinter is installed, pops up a file chooser window.
        
        If changeFolder(), file chooser window will begin with your 
        current folder, so you can use changeFolder() to find
        out where you are.
        
        If changeFolder(fileName), your current folder becomes
        fileName BEFORE the file chooser window pops up.
    """
    if tk==None:
        if folderName==None:
           print \
             "must give a folder name because Tkinter is missing"
        else:
           try: 
               os.chdir(folderName)
           except:
               print "cannot change to %s" % folderName
    else:
        if folderName!=None:
            try: os.chdir(folderName)
            except: pass
        import tkFileDialog as tf
        root = tk.Tk()
        root.wait_visibility(root)
        root.wm_attributes("-topmost", 1)
        root.wm_attributes("-toolwindow",1)
        chooser = tf.Directory(root)
        root.withdraw()
        fileName = chooser.show()
        if fileName!='': os.chdir(fileName)
        
def savable():
    """
    Usage: savable()
    
    Shows you a report of what is savable in existing workspace.
    
    If something appears that you do not want to save, delete it
    before saving as in
    
    del x
    """
    print "\nGlobal Variables:", _savableVariables()
    defs = _savableDefinitions()
    print "Functions:", filter(lambda x: _ISTYPE(x,FUNTYPE), defs)
    print "Classes:", \
          filter(lambda x: _ISTYPE(x,CLASSTYPE), defs)
    print "Modules:", _savableModules(), "\n"
    
def save(fileName='unnamed_workspace'):
    """
    Usage: save() or save(<<workspace name>>)
    
    Saved stuff can be restored later with the load function.
    Be sure you are in the folder you want the saving to happen.
    Use changeFolder() to get you there or to see where you are.
    
    Savable are 
         modules you have imported with import <modulename>,
         variables that refer to primitives or lists, tuples, and dicts
         functions (or classes) defined with the edit function
         
    Put another way: if you are into anything esoteric, you probably
    won't be able to save it.
    """
    try:
        f = open(fileName+".pws","w")
    except:
        print "cannot save to workspace " + fileName
        return
    cPickle.dump(_savableModules(),f)
    names = _savableVariables()
    cPickle.dump(names,f)
    for n in names: 
        exec  "p.dump(%s,f)" % n  in  _Global, {'f':f,'p':cPickle}
    names = _savableDefinitions()
    cPickle.dump( names, f )
    for n in names: cPickle.dump(_Defs[n],f)
    f.close()
    print "saved to",fileName

def load(fileName='unnamed_workspace'):
    """
    Usage: load() or load(<<workspace name>>)
    
    Use changeFolder() to get to the folder where the workspace
    was stored.
    """
    global _Defs, _Global
    try:
        f = open(fileName+".pws")
    except:
        raise _WorkspaceError(
            "cannot find the workspace " + fileName )
    while True:
        clear = raw_input( \
                  "Clear the existing workspace before loading?" \
                  + " (answer y, n, or ?) " 
                )
        if clear=='y' or clear=='Y':     
            _clearWorkspace()
            break
        elif clear=='n' or clear=='N':
            break
        else:
            print "\nAnswering with the letter y will cause all the savable" \
               + "\nvariables, functions, and classes you have defined"\
               + "\in (or loaded into) this workspace to be forgottten."\
               + "\nClearing the workspace only affects what is " \
               + "\ncurrently known by the Python interpreter.  It has"\
               + "\nno effect on any saved workspaces." \
               + "\n\nAnswering with the letter n will not forget " \
               + "\nanything before the loading process starts. " \
               + "\nHowever, if the loaded workspace uses an identifier"\
               + "\nthat is currently in use, then the current meaning of"\
               + "\nthat identifier will be lost.\n"
    modules = cPickle.load(f)
    showMods = []
    if modules:
        wantMods = raw_input( "Reload saved modules? (y, n)" )
        if wantMods=='y' or wantMods=='Y':
            print "Trying to reload saved modules.  Only modules " \
                  + "that were imported with a simple\n" \
                  + "import <<module name>>\n" \
                  + "will work correctly."
            for m in modules:
                try:
                    exec  "import " + m  in  _Global
                    showMods.append(m)
                except:
                    print "could not import m"
    varNames = cPickle.load(f)
    for n in varNames:
        exec "global " + n + ";" + \
             n + "= p.load(f)" \
        in _Global, {'f':f, 'p':cPickle }
    names = cPickle.load(f)
    for n in names:
        _Defs[n] = cPickle.load(f)
        exec _Defs[n] in _Global

    print \
     "\nThe following have been loaded from the %s workspace" % fileName
    print "(If you cleared the current workspace, " \
          "the following is all there is.)\n"
    print "Global Variables:",varNames
    print "Functions:", 
    print filter( lambda x: _ISTYPE(x,FUNTYPE), names )
    print "Classes:",
    print filter( lambda x: _ISTYPE(x,CLASSTYPE), names )
    print "Modules:", showMods, "\n"
    f.close()

def edit(fun=None):
    """
    Usage: edit() or edit(<<name>>)

    Pops up a window in which you can edit and compile a function
    (or class) named <<name>>.  In that window, you enter the function
    exactly as you would in the interpreter.  
    
    The advantages over using the Python interpreter directly are better
    editing options, the ability to re-edit previously created
    functions and classes, and the ability to save and reload functions.
    
    Short said: edit, save, and load  replace an IDE for simple
    applications and general fooling around.
    """
    if tk==None:
	    raise NotImplementedError, \
	    "without Tkinter, there is no implementation of edit"
    root = tk.Tk()
    w = _Define(root,fun)
    root.wait_visibility(root) # required to make attribute changes work
    root.focusmodel("active")  # until we force focus on the text entry
    root.mainloop()
    if w.Compiled:
        print "%s edited and compiled OK" %  w.name
    else:
        print "definition failed"

if tk:

  class _Define:
    name = '<<unnamed name>>'
    
    def __init__(self,master,fun):
        global _Defs, _Global
        if fun==None:
            self.Compiled = False
            text = ""
            message = \
                "enter a function (or class) definition\nand check it"
        elif type(fun)==FUNTYPE and _Defs.has_key(fun.func_name):
              self.Compiled = True
              text =_Defs[fun.func_name]
              self.name = fun.func_name
              message = _ALL_OK % self.name
        elif type(fun)==CLASSTYPE and _Defs.has_key(fun.__name__):
              self.Compiled = True
              text =_Defs[fun.__name__]
              self.name = fun.__name__
              message = _ALL_OK % self.name
        else:
                raise _WorkspaceError( \
                    "It seems you are trying to edit something that"\
                      + " already exists but has not been previously"\
                      + " edited.  That isn't possible.  Try using "\
                      + 'del <<whatever you want to edit>>'\
                      + " first if you want a fresh start." \
                )
                master.destroy()
                self.Compiled = False
        self.textEntry = tk.Text( \
                master, \
                font=("Courier",8),\
                width=72,height=16, \
                takefocus=True
        )
        self.textEntry.insert("1.0",text)
        self.button1 = tk.Button( \
              master, \
              text="check", \
              command=self.compile \
        )
        self.button3 = tk.Button( \
              master, \
              text="load", \
              command=self.buttonFinish
        )
        self.errorMsg = tk.Text( \
                master, \
                font=("Courier",8),\
                width=72,height=14, \
                takefocus=False
        )
        self.errorMsg.insert("1.0",message)
        for r in range(7):
            for c in range(5):
               if (r,c) not in \
                [(1,1),(1,2),(1,3),(3,1),(3,3),(5,1),(5,2),(5,3)]:
                  x = tk.Label(master,text=' ')
                  x.grid( row=r, column=c )
        self.textEntry.grid( row=1, column=1, columnspan=3 )
        self.button1.grid( row = 3, column = 1 )
        self.button3.grid( row = 3, column = 3 )
        self.errorMsg.grid( row = 5, column = 1, columnspan=3 )
        master.bind('<Destroy>', self.notWaiting)
        self.textEntry.bind('<KeyRelease>',self.keyReleased)
        master.title('Function or Class Editing Window')
        self.textEntry.focus_force() # why we needed 'active'
        master.focusmodel('passive') # be nice
        self.master = master

    def compile(self):
        global _Defs, _Global
        text = self.textEntry.get("1.0","end")
        sob = _BEGIN2(text)
        self.errorMsg.delete("1.0","end")
        try:
            try:
                (defclass,name) = (sob.group(1),sob.group(2))
            except:
               raise SyntaxError( \
                  "expect: def <<name>>: or class <<name>>:" )
            if  defclass not in ('def','class'):
               raise SyntaxError( \
                  "expect: def %s: or class %s:" % (name,name) )
            if _NO_NO( text ):
                raise SyntaxError( \
                    "only one global entity may be edited")
            if _Global.has_key(name): del _Global[name]
            exec text  in  _Global
            self.Compiled = True
            _Defs[ name ] = text
            self.name = name
            message = _ALL_OK % name
        except:
            self.Compiled = False
            message = traceback.format_exc()
        self.errorMsg.insert("1.0",message)
     
    def keyReleased(self,event):
        if self.Compiled:
            self.errorMsg.delete("1.0","end")
            self.errorMsg.insert( "1.0", "" )
        self.Compiled = False
        
    def notWaiting(self,event):
        self.master.quit()  # dunno why this is needed
        
    def buttonFinish(self):
        if self.Compiled:
            self.master.destroy()
        else:
            self.errorMsg.delete("1.0","end")
            self.errorMsg.insert( \
                "1.0", \
                "Cannot load into the interpreter " \
                + "until you successfully check." \
            )

def readString(fileName):
    """ reads the named file into a string and returns it """
    fi = open(fileName)
    txt = fi.read()
    fi.close()
    return txt

def writeString(fileName,string):
    """ causes the contents of the named file to be the given string """
    fo = open(fileName,'w')
    fo.write(string)
    fo.close()
    
if sys.platform=='win32' and os.getcwd()[-7:]=="Desktop": os.chdir("..")
major,minor = sys.version[0],sys.version[2]
if major<2 or (major==2 and minor<4):
    raise _WorkspaceError( _PLEASE_UPGRADE )
    
print \
 " changeFolder, savable, save, load, edit, readStr, writeStr."
print "For more information use help(changeFolder), etc.\n"

_Global['savable'] = savable
_Global['save'] = save
_Global['load'] = load
_Global['readStr'] = readString
_Global['writeStr'] = writeString
_Global['edit'] = edit
_Global['changeFolder'] = changeFolder

