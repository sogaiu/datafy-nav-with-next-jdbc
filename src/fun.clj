(ns fun
  (:require [next.jdbc :as nj]))

(def db
  {:dbtype "h2"
   :dbname "example"})

(def ds
  (nj/get-datasource db))

(nj/execute! ds ["
  drop table if exists address
"])

(nj/execute! ds ["
  create table address (
     id int auto_increment primary key,
     name varchar(32),
     email varchar(255)
  )
"])

(nj/execute! ds ["
  insert into address(name, email)
    values('Sherlock Holmes', 'sherlock@hemlock.org')
"])

(nj/execute! ds ["
  drop table if exists contact
"])

(nj/execute! ds ["
  create table contact (
     id int auto_increment primary key,
     address_id int,
     descr varchar(32),
     method_str varchar(32)
  )
"])

(nj/execute! ds ["
  insert into contact(address_id, descr, method_str)
    values(1, 'mobile phone', '555-1239')
"])

(nj/execute! ds ["
  insert into contact(address_id, descr, method_str)
    values(1, 'skype', 'watsonfriend')
"])

(nj/execute-one! ds ["
  insert into address(name, email)
    values('John Watson', 'watson@crick.com')
"] {:return-keys true})

(comment

  ;; load the content outside of this comment block first
  
  (in-ns 'fun)
  
  (require '[next.jdbc :as nj])

  ;; start rebl
  (require '[cognitect.rebl :as cr])
  (cr/ui) ; after a slight wait, rebl's gui should appear

  ;; send contact table to rebl
  (cr/submit "select * from contact" ; like a comment
    (nj/execute! ds ["
      select * from contact
    "]))

  ;; in rebl, note that a new entry has shown up in the 'browse' tab in
  ;; the bottom left pane of the gui (headers: expr, val, msecs, source)
  
  ;; in the browse pane, select the entry and click on the right
  ;; arrow (>) at the bottom of the gui (hovering may display a keyboard
  ;; shortcut and other explanation)
  
  ;; the right pane should now display a datafy-ed version of the value
  ;; (from the entry selected previously in the browse pane)
  
  ;; in the lower right pane, select the entry with
  ;; key :CONTACT/ADDRESS_ID and click on the right arrow (>) again

  ;; the right pane should now display the result of calling nav
  ;; with key and value from the previously selected row -- in this case,
  ;; info from the address (not contact) table is now displayed

  ;; note that we just "nav"-ed from the contact table to the address table


  ;; bonus fun with stuary halloway's reflector
  (require '[com.stuarthalloway.reflector :as csr])

  ;; another way to send info to rebl is to use tap>
  (tap> (csr/on (find-ns 'clojure.core)))

  ;; select the 'tap' tab (next to 'out' which is next to 'browse') in
  ;; rebl and click on the browse button
  
  ;; now look at the browse tab and notice a new entry with expr :rebl/taps

  ;; select it and click on the right arrow (>)

  ;; have fun exploring the clojure.core namespace -- specifically, check
  ;; out the :resources and :pom entries which reflector has added
  
  )
