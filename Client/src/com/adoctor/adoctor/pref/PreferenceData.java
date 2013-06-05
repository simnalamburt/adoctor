package com.adoctor.adoctor.pref;

import static org.msgpack.template.Templates.TInteger;
import static org.msgpack.template.Templates.TString;
import static org.msgpack.template.Templates.tMap;

import java.io.IOException;
import java.util.Map;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.template.Template;
import org.msgpack.unpacker.Unpacker;

/**
 * 어플리케이션 Preference 정보 클래스
 * @author Hyeon
 */
public class PreferenceData implements MessagePackable {
	public int age;
	public int job;
	public int sex;
	
	public PreferenceData(int Age, int Job, int Sex)
	{
		age = Age; job = Job; sex = Sex;
	}

	// MessagePack
	/**
	 * PreferenceData 시리얼라이저
	 */
	@Override
	public void writeTo(Packer packer) throws IOException {
		packer.writeMapBegin(3);
		packer.write("age");
		packer.write(age);
		
		packer.write("job");
		packer.write(job);
		
		packer.write("sex");
		packer.write(sex);
		packer.writeMapEnd();
	}
	/**
	 * PreferenceData 디시리얼라이저
	 */
	@Override
	public void readFrom(Unpacker unpacker) throws IOException {
		Template<Map<String, Integer>> mapTmpl = tMap(TString, TInteger);
		Map<String, Integer> map = unpacker.read(mapTmpl);
		
		age = map.get("age");
		job = map.get("job");
		sex = map.get("sex");
	}
}
