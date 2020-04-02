(ns essen-editor.parser
  (:require
   [clojure.core.match :refer [match]]
   [instaparse.core :as insta :refer-macros [defparser]]))



(defparser clojure-parser
  (str
   " <S> = expression*"
   " <expression> = <comma> | deref | ignore | <ws> | <nl> | symbol | ns-symbol | anon-function | anon-function-arg | set | map | vector | atom | list | quotes-list | keyword | ns-keyword | rest | comment | reader | short-metadata | metadata | ns-function"
   " list = <'('> expression* <')'> "
   " anon-function = <'#('> expression* <')'> "
   " anon-function-arg = '%' "
   " quotes-list = <'\\'('> expression* <')'> "
   " vector = <'['> expression* <']'> "
   " ns-function = name'/'name "
   " set = <'#{'> expression* <'}'> "
   " map = <'{'> expression* <'}'> "
   " reader = '#' #'[^\\(]*' <'('> expression* <')'> "
   " comment = <';'> #'.*' nl "
   " <nl> = '\\n' "
   " <ws> = ' ' "
   " <atom> = number | string | name "
   " rest = '&' "
   " ignore = '_' "
   " deref = '@'name "
   " number = #'\\d+' "
   " string = <'\\\"'> (#'[^\"\\\\]+' | escaped-char)* <'\\\"'> "
   " <escaped-char> = #'\\\\.' "
   " <comma> = <','> "
   " metadata = <'^{'> expression* <'}'> "
   " short-metadata = #'\\^'keyword"
   " ns-keyword = #'\\:'keyword"
   " symbol = #'\\''name"
   " ns-symbol = #'\\''name/name"
   " keyword = #'\\:'name"
   " name = #'[a-zA-Z\\+-\\=]([0-9a-zA-Z!\\>\\?\\.\\'\\+-]*)' "))

(def example
  (str
   '(defn hello [world] (goobye world "what"))
   '(goodbye [world] world)
   '(def all-the-way)
   '(defn goodbye [world] world)))

(defn get-defn-name [expr]
  (match expr
    [:list [:name (:or "defn" "defn-")] [:name defn-name] & _]
    defn-name
    :else nil))

(defn get-ns-name [expr]
  (match expr
    [:list [:name "ns"] [:name ns-name] & _]
    ns-name
    :else nil))

(defn ns-expr? [expr]
  (some? (get-ns-name expr)))

(defn defn-expr? [expr]
  (some? (get-defn-name expr)))

(defn defn-key-meta [namespace expr]
  {(keyword namespace (get-defn-name expr))
   (meta expr)})

(defn file->functions [contents]
  (let [e (clojure-parser contents)
        n (get-ns-name (first (filter ns-expr? e)))]
    (->> e
         (filter defn-expr?)
         (map (partial defn-key-meta n))
         (into {}))))

(set! *print-namespace-maps* false)

(defn replace-defn [file-name f s]
  ;; (let [contents (slurp file-name)
  ;;       {:instaparse.gll/keys [start-index end-index]}
  ;;       (get (file->functions contents) f)]
  ;;   (spit file-name
  ;;         (str
  ;;          (subs contents 0 start-index )
  ;;          s
  ;;          (subs contents end-index))))
  )
