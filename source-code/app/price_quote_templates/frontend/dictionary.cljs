
(ns app.price-quote-templates.frontend.dictionary)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:body-description-helper
           {:en "The description of the body could contain the following items: @current-price @currency @vat-value"
            :hu "A törzs megjegyzése a következő elemeket tartalmazhatja: @arfolyam @penznem @afa-ertek"}
           :body-description-placeholder
           {:en "The quoted prices are ..."
            :hu "Az árajánlatban feltüntetett árak ..."}
           :body-subtitle-helper
           {:en "The subtitle of the body could contain the following items: @vehicle-count @model-name @model-product-description @type-name"
            :hu "A törzs alcíme a következő elemeket tartalmazhatja: @jarmu-darabszama @modell-neve @modell-termek-megnevezese @tipus-neve"}
           :body-subtitle-placeholder
           {:en "Thank your for your inquiry of ..."
            :hu "Köszönjük megkeresését ..."}
           :body-title-helper
           {:en "The title of the body could contain the following items: @first-name @last-name"
            :hu "A törzs címe a következő elemeket tartalmazhatja: @vezeteknev @keresztnev"}
           :body-title-placeholder
           {:en "Dear @first-name @last-name,"
            :hu "Tisztelt @vezeteknev @keresztnev!"}
           :footer-content-helper
           {:en "The content of the footer could contain the following items: @tab"
            :hu "A lábléc tartalma a következő elemeket tartalmazhatja: @tab"}
           :footer-content-placeholder
           {:en "The content of the footer could contain the following items: @tab"
            :hu "A lábléc tartalma a következő elemeket tartalmazhatja: @tab"}
           :issuer-details-helper
           {:en "The description of the issuer could contain the following items: @tab"
            :hu "A kibocsátó leírása a következő elemeket tartalmazhatja: @tab"}
           :issuer-details-placeholder
           {:en "The issuer's address and contacts"
            :hu "A kibocsátó címe és elérhetőségei"}
           :issuer-name-placeholder
           {:en "The issuer's name"
            :hu "A kibocsátó neve"}
           :price-quote-template
           {:en "Price quote template"
            :hu "Árajánlat sablon"}
           :price-quote-templates
           {:en "Price quote templates"
            :hu "Árajánlat sablonok"}
           :price-quote-template-name-placeholder
           {:en "Template name"
            :hu "Sablon neve"}
           :search-in-price-quote-templates
           {:en "Search in templates"
            :hu "Keresés a sablonok között"}
           :select-price-quote-template!
           {:en "Select template"
            :hu "Sablon kiválasztása"}
           :select-price-quote-templates!
           {:en "Select templates"
            :hu "Sablonok kiválasztása"}
           :unnamed-price-quote-template
           {:en "Unnamed template"
            :hu "Névtelen sablon"}})
