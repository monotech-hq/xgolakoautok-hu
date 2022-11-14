
(ns site.xgo.utils.url
  (:require
    [re-frame.core :as r]
    [ajax.url :as ajax-url]
    [clojure.string :refer [split blank? replace]]))

(defn go-to! [url]
  (.replace (.-location js/window)
            url))

(defn set-url! [url]
  (let [history (.-history js/window)]
    (.replaceState history nil nil url)))

(defn decode-uri [uri]
  (.decodeURI js/window uri))

(defn route-params->search-term [route-params]
  (if (empty? (:s route-params))
    ""
    (decode-uri (:s route-params))))

(defn map->query-params-str [params]
  (ajax-url/params-to-str :rails params))

(defn url-query-params [url config]
  (let [query-params-str (map->query-params-str config)]
    (str url "?" query-params-str)))

(defn- acc-param [o v]
  (cond
    (coll? o) (vec (concat o v))
    (some? o) [o v]
    :else     v))

(defn contains-map? [key]
  (re-find #"\[.+\]" key))

(defn contains-vec? [key]
  (re-find #"\[\]" key))

(defn query-map-keys->keywords [keys]
  (mapv keyword (split keys #"\]\[|\[|\]")))

(defn query-vec-key->keyword [key]
  (keyword (replace key #"\[\]" "")))

(defn acc-keyword [n-map key value]
  (cond
    (contains-map? key) (update-in n-map (query-map-keys->keywords key) acc-param value)
    (contains-vec? key) (update n-map (query-vec-key->keyword key) acc-param [value])
    :else (update n-map (keyword key) acc-param value)))

(defn query-params->map
  "Parse `s` as query params and return a hash map."
  [url]
  (if-not (nil? (re-find #"\?" url))
    (let [query-string (last (split (decode-uri url) "?"))]
      (reduce #(let [[key value] (split %2 #"=")]
                 (acc-keyword %1 key value))
       {}
       (split (str query-string) #"&")))
    {}))

;; -----------------------------------------------------------------------------
;; ---- Side effects ----

(r/reg-fx
  :url/go-to!
  (fn [url]
    (go-to! url)))

(r/reg-fx
  :url/set-url!
  (fn [url]
    (set-url! url)))

;; ---- Side effects ----
;; -----------------------------------------------------------------------------
