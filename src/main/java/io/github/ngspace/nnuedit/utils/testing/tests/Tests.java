package io.github.ngspace.nnuedit.utils.testing.tests;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.swing.Icon;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.asset_manager.AssetManager;
import io.github.ngspace.nnuedit.asset_manager.extensions.ExtensionManager;
import io.github.ngspace.nnuedit.utils.FileIO;
import io.github.ngspace.nnuedit.utils.ImageUtils;
import io.github.ngspace.nnuedit.utils.testing.TestOrder;
import io.github.ngspace.nnuedit.utils.testing.TestResult;
import io.github.ngspace.nnuedit.utils.testing.TestScope;
import io.github.ngspace.nnuedit.utils.testing.test_types.BoolTest;
import io.github.ngspace.nnuedit.utils.testing.test_types.Test;

/**
 * You may wonder why I made an entire package for test units and a whole system for loading tests anywhere on the
 * project. Well!... fuck you.
 */
public class Tests {private Tests() {}
	/**
	 * Load settings so other tests won't crash and burn.
	 */
	@TestOrder(0)
	public static class SettingsTest implements BoolTest<IOException> {
		@Override public boolean run() {
			try {
				Main.loadSettings();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return false;
			}
			return true;
		}
	}
	/**
	 * Prepare the system.
	 */
	@TestOrder(-1)
	public static class PreExecTest implements BoolTest<IOException> {
		@Override public boolean run() {
			try {
				Main.preExec(true, true);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return false;
			}
			return true;
		}
	}
	public static class AssetLoadingTest implements Test<IOException> {
		@Override public TestResult runTest() throws IOException {
			AssetManager.addDefaultIcons();
			int sum = AssetManager.icons.size();
			for (Icon icon : AssetManager.icons.values())
				if (icon==ImageUtils.getMissingIcon())
					sum--;
			return new TestResult(sum,AssetManager.icons.size());
		}
	}
	public static class ExtensionsTest implements BoolTest<IOException> {
		@Override public boolean run() {
			try {
				ExtensionManager.loadExtensions(false);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	/**
	 * Make sure NNUEdit is loaded last to make sure everything is already loaded
	 */
	@TestOrder(9)
	public static class AppTest implements BoolTest<IOException> {
		@Override public boolean run() throws IOException {
			Main.hook = () -> false;
			App app = new App("");
			app.setVisible(true);
			app.close();
			return true;
		}
	}
	/**
	 * Check if file IO works
	 */
	public static class FileIOTest implements Test<IOException> {
		@Override public TestScope getTestScope() {return TestScope.DEEP;}
		@Override public TestResult runTest() throws IOException {
			int succesful = 10;
			int Tests = succesful;
			String fakepath = "fake\folder/notrealfile.rustisbad";
			String testfile = "זה הוא test";
			if (!"rustisbad".equals(FileIO.getFileExt(fakepath))) succesful--;
			if (!"notrealfile".equals(FileIO.getFileNameWOExt(fakepath))) succesful--;
			if (!"notrealfile.rustisbad".equals(FileIO.getFileName(fakepath))) succesful--;
			if (!"".equals(FileIO.getFileType(fakepath))) succesful--;
			if (!"img".equals(FileIO.getFileType("png.png"))) succesful--;
			if(!"UTF-8".equals(FileIO.guessEncoding(testfile.getBytes()))) succesful--;
			
			//Read, Write, Delete
			
			try {
				FileIO.save(new File("testfile"),testfile);
			} catch (Exception e) {succesful--;e.printStackTrace();}
			try {
				if (!Objects.equals(FileIO.read(new File("testfile")), testfile)) succesful--;
			} catch (Exception e) {succesful--;e.printStackTrace();}
			try {
				File f = new File("testfolder\\this/is\\a/test/folder");
				f.mkdirs();
				FileIO.recursiveDelete(new File("testfolder").toPath());
				if (f.exists()) succesful--;
			} catch (Exception e) {succesful--;e.printStackTrace();}
			try {FileIO.getProgramPath();} catch (Exception e) {succesful--;e.printStackTrace();}
			
			return new TestResult(succesful, Tests);
		}
	}
}
