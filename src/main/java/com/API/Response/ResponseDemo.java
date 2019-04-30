package com.API.Response;

import java.util.List;

import org.apache.commons.lang.Validate;

public class ResponseDemo {
 private String name;
 private int salary;
 private int age;
 private int id;
 private String getname(){
	 return name;
 }
 public void  setname(String name_1){
	 this.name=name_1;
	 
 }
 private int getsalary() {
	 return salary;
 }
 public void  setsalary(int salary_1) {
	 this.salary=salary_1;
	 
 }
 private int getage() {
	 return age;
 }
 public void  setage(int age_1) {
	 this.age=age_1;
 }
	 private int getid() {
		 return id;
	 }
	 public void  setid(int id_1) {
		 this.id=id_1;
	 
 }
	

}
