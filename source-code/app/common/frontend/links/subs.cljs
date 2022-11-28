

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-page-public-link
  ; @param (string) page-name
  ;
  ; @example
  ;  (r get-page-public-link db "My page")
  ;  =>
  ;  "https://my-app.com/my-page"
  [db [_ page-name]]
  (let [normalized-name (normalize/clean-text page-name)
        public-link     (str "/"normalized-name)]
       (r use-app-domain db public-link)))
