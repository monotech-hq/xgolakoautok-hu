
(ns app.common.backend.links.helpers
    (:require [normalize.api :as normalize]
              [re-frame.api  :as r]
              [string.api    :as string]
              [uri.api       :as uri]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-link
  ; @param (string) link
  ;
  ; @usage
  ;  (valid-link "/my-page")
  ;
  ; @example
  ;  (valid-link "/my-page")
  ;  =>
  ;  "https://my-domain.com/my-page"
  ;
  ; @example
  ;  (valid-link "my-page")
  ;  =>
  ;  "https://my-domain.com/my-page"
  ;
  ; @example
  ;  (valid-link "My page")
  ;  =>
  ;  "https://my-domain.com/my-page"
  ;
  ; @example
  ;  (valid-link "something.com")
  ;  =>
  ;  "https://something.com"
  ;
  ; @return (string)
  [link]
  (let [app-domain @(r/subscribe [:x.core/get-app-config-item :app-domain])
        link        (string/to-lowercase link)]))
       ;(if-let [domain (uri/uri->domain link)]


        ;normalized-link (str (-> link (string/to-first-occurence    "/" {:return? false}))
        ;                     (-> link (string/after-first-occurence "/" {:return? true})
        ;                              (normalize/clean-text         "/-"))]




       ;(letfn [(trim [n] (-> n (string/after-first-occurence  "://"  {:return? true})
        ;                       (string/after-first-occurence  "www." {:return? true})
        ;                       (string/before-first-occurence "#"    {:return? true})
        ;                       (string/before-first-occurence "?"    {:return? true}))
        ;      (let [trimmed-app-domain (trim app-domain)
        ;            trimmed-link       (trim link)
        ;            link               (string/not-starts-with! trimmed-link trimmed-app-domain)
        ;           (cond (string/starts-with? link "/"))


        ;normalized-link (normalize/clean-text link)]
       ;(cond (string/starts-with?   link "/") (str app-domain     link)
        ;     (string/contains-part? link ".") (uri/valid-uri      link)
        ;     :else                            (str app-domain "/" link)]))

(defn link-reserved?
  ; @param (string) link
  ;
  ; @usage
  ;  (link-reserved? "/my-page")
  ;
  ; @return (boolean)
  [link]
  ; link: "my-page"
  ; 1. "my-page"                       => "https://my-domain.com/my-page"
  ; 2. "https://my-domain.com/my-page" => "/my-page"
  ;
  ; link: "/my-page"
  ; 1. "/my-page"                      => "https://my-domain.com/my-page"
  ; 2. "https://my-domain.com/my-page" => "/my-page"
  ;
  ; link: "https://my-domain.com/my-page"
  ; 1. "https://my-domain.com/my-page" => "https://my-domain.com/my-page"
  ; 2. "https://my-domain.com/my-page" => "/my-page"
  (let [path (-> link (valid-link)
                      (uri/to-path))]
      @(r/subscribe [:x.router/route-template-reserved? path])))
