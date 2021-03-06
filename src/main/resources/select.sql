select this_.apexClass as y0_, this_.enabled as y1_ from class_accesses this_ where this_.profile like '%2_1 - Inside Sales - Sales Insight%' order by y0_ asc;

select this_.profile as y0_, this_.enabled as y1_ from user_permissions this_ where this_.name like '%DistributeFromPersWksp%' order by y0_ asc;

select * from user_permissions;

select distinct name from USER_PERMISSIONS order by name;

select profile, enabled from CLASS_ACCESSES where apexclass like 'Utils' order by profile;

select * from CLASS_ACCESSES;

select distinct apexclass from CLASS_ACCESSES order by apexclass;

select profile, enabled from CLASS_ACCESSES where apexclass like '%AEAckDetailPageControllerTest%' order by profile asc;

select layout, coalesce (recordtype,'') as recordtype from LAYOUT_ASSIGNMENTS;

select layout, recordtype from LAYOUT_ASSIGNMENTS;

select * from RECORD_TYPE_VISIBILITIES;

select * from FIELD_PERMISSIONS;

select * from FIELD_PERMISSIONS order by profile asc;

select * from TAB_VISIBILITIES order by PROFILE asc;

select this_.editable as y0_, this_.readable as y1_, this_.field as y2_ from field_permissions this_ where this_.profile like '2_1 - Inside Sales - Sales Insight%' order by y2_ asc;

select editable, readable, field from FIELD_PERMISSIONS where name like 'Standard.profile' order by field asc;

select editable, readable, profile from FIELD_PERMISSIONS where field like 'SVMXC__Service_Order__c.Rejected_By__c' order by profile asc;

select distinct substr(profile,1,locate('.profile',profile) - 1) profile_name from FIELD_PERMISSIONS;

select distinct profile from FIELD_PERMISSIONS order by profile asc;

select distinct field from field_permissions order by field asc;

select distinct p.name profile, count(f.field) no_of_fields from profile p, field_permissions f where p.name = f.name group by p.name order by p.name asc; 

select p.name profile, f.field field, f.readable readable, f.editable editable from profile p, field_permissions f where p.name like 'Standard.profile' and p.name = f.name group by p.name, f.field, f.readable, f.editable order by p.name asc;

select * from field_permissions f left join profile p on f.name = p.name;